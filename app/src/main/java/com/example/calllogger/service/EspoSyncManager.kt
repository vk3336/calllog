package com.example.calllogger.service

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.calllogger.data.AppDatabase
import com.example.calllogger.data.CallLogEntity
import com.example.calllogger.network.EspoApiService
import com.example.calllogger.network.EspoCallRequest
import com.example.calllogger.util.ConfigManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import java.text.SimpleDateFormat
import java.util.*

class EspoSyncManager(private val context: Context) {

    private val mutex = Mutex()
    private val database = AppDatabase.getDatabase(context)
    private val configManager = ConfigManager.getInstance(context)

    companion object {
        private const val TAG = "EspoSyncManager"
    }

    /**
     * Sync all pending calls. Uses a Mutex so only one sync runs at a time.
     * If already running, the new call is silently dropped.
     */
    suspend fun syncPendingCalls(espoApi: EspoApiService) {
        if (!mutex.tryLock()) {
            Log.d(TAG, "Sync already in progress, skipping this cycle")
            return
        }
        try {
            doSync(espoApi)
        } finally {
            mutex.unlock()
        }
    }

    private suspend fun doSync(espoApi: EspoApiService) {
        // Reset failed records (max 3 attempts) back to PENDING so they are retried
        val resetCount = database.callLogDao().resetFailedToPending()
        if (resetCount > 0) {
            Log.d(TAG, "♻️ Reset $resetCount failed records back to PENDING for retry")
        }

        val pending = database.callLogDao().getPendingCallLogs()
        Log.d(TAG, "Found ${pending.size} pending calls to sync")

        if (pending.isEmpty()) {
            broadcast("✅ All Synced", "No pending calls to sync")
            return
        }

        // Batch size: 20 calls processed concurrently per batch
        val batchSize = 20
        val batches = pending.chunked(batchSize)

        broadcast("🔄 Starting Batch Sync", "Total: ${pending.size} calls\nBatches: ${batches.size} (${batchSize} per batch)")

        for ((batchIndex, batch) in batches.withIndex()) {
            Log.d(TAG, "Processing batch ${batchIndex + 1}/${batches.size} (${batch.size} calls)")

            // All calls in this batch run concurrently — same HTTP connection pool reused
            coroutineScope {
                batch.map { call ->
                    async {
                        // Atomic claim — only one coroutine can claim a row
                        val claimed = database.callLogDao().claimForSync(call.id)
                        if (claimed == 0) {
                            Log.d(TAG, "Call ${call.id} already claimed, skipping")
                            return@async
                        }

                        try {
                            val request = buildRequest(call)

                            // Duplicate check against EspoCRM
                            val dateStart = request.dateStart
                            val cUserPhone = request.cUserPhone
                            if (!dateStart.isNullOrBlank() && !cUserPhone.isNullOrBlank()) {
                                try {
                                    val searchResp = espoApi.searchCalls(dateStart = dateStart, cUserPhone = cUserPhone)
                                    if (searchResp.isSuccessful && (searchResp.body()?.total ?: 0) > 0) {
                                        val existingId = searchResp.body()?.list?.firstOrNull()?.id ?: "duplicate"
                                        Log.d(TAG, "Duplicate found in ESPO, skipping POST (id=$existingId)")
                                        database.callLogDao().markAsSynced(call.id, existingId)
                                        return@async
                                    }
                                } catch (e: Exception) {
                                    Log.w(TAG, "Duplicate check failed, proceeding with POST: ${e.message}")
                                }
                            }

                            // POST to EspoCRM
                            val response = espoApi.createCall(request)

                            if (response.isSuccessful) {
                                val espoCall = response.body()
                                if (espoCall != null) {
                                    database.callLogDao().markAsSynced(call.id, espoCall.id)
                                    Log.d(TAG, "✅ Synced call ${call.id} → ESPO ID: ${espoCall.id}")
                                } else {
                                    database.callLogDao().markAsFailed(call.id, System.currentTimeMillis())
                                    Log.w(TAG, "⚠️ Empty response for call ${call.id}")
                                }
                            } else {
                                database.callLogDao().markAsFailed(call.id, System.currentTimeMillis())
                                val errorBody = response.errorBody()?.string() ?: "No error details"
                                Log.e(TAG, "❌ Failed call ${call.id}: HTTP ${response.code()} - $errorBody")
                            }

                        } catch (e: Exception) {
                            database.callLogDao().markAsFailed(call.id, System.currentTimeMillis())
                            Log.e(TAG, "❌ Exception syncing call ${call.id}: ${e.message}")
                        }
                    }
                }.forEach { it.await() }
            }

            // Small pause between batches so we don't flood the server
            if (batchIndex < batches.size - 1) {
                kotlinx.coroutines.delay(500)
            }
        }

        // Final summary broadcast
        val syncedCount = database.callLogDao().getSyncedCallLogCount()
        val totalCount = database.callLogDao().getCallLogCount()
        val stillFailed = database.callLogDao().getFailedCallLogsForRetry().size
        val summaryMsg = buildString {
            appendLine("Synced: $syncedCount / $totalCount")
            appendLine("Processed ${batches.size} batch(es)")
            if (stillFailed > 0) appendLine("⚠️ $stillFailed failed (will retry next hour)")
        }
        broadcast("✅ Batch Sync Complete", summaryMsg)

        configManager.lastSyncTime = System.currentTimeMillis()
    }

    private fun buildRequest(call: CallLogEntity): EspoCallRequest {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val callStartTime = dateFormat.format(Date(call.timestamp))

        val direction = when (call.callType) {
            CallLogEntity.INCOMING_TYPE,
            CallLogEntity.MISSED_TYPE,
            CallLogEntity.REJECTED_TYPE,
            CallLogEntity.BLOCKED_TYPE -> "Inbound"
            CallLogEntity.OUTGOING_TYPE -> "Outbound"
            else -> "Inbound"
        }

        val status = when {
            call.duration > 0 && call.callType == CallLogEntity.INCOMING_TYPE  -> "Held"
            call.duration > 0 && call.callType == CallLogEntity.OUTGOING_TYPE  -> "Held"
            call.callType == CallLogEntity.ANSWERED_EXTERNALLY_TYPE            -> "Held"
            call.callType == CallLogEntity.MISSED_TYPE                         -> "Not Held"
            call.callType == CallLogEntity.REJECTED_TYPE                       -> "Not Held"
            call.callType == CallLogEntity.BLOCKED_TYPE                        -> "Not Held"
            call.callType == CallLogEntity.VOICEMAIL_TYPE                      -> "Not Held"
            call.callType == CallLogEntity.OUTGOING_TYPE && call.duration == 0L -> "Not Held"
            call.callType == CallLogEntity.INCOMING_TYPE && call.duration == 0L -> "Not Held"
            else -> {
                Log.w(TAG, "Unknown call type: ${call.callType}, defaulting to Not Held")
                "Not Held"
            }
        }

        val callName = buildString {
            append(call.getCallTypeString())
            append(" call")
            if (!call.contactName.isNullOrBlank()) append(" with ${call.contactName}")
            else append(" - ${call.phoneNumber}")
        }

        return EspoCallRequest(
            name = callName,
            status = status,
            direction = direction,
            phone = call.phoneNumber,
            description = null,
            cSeconds = call.duration.toInt(),
            deleted = false,
            dateStart = callStartTime,
            duration = null,
            reminders = emptyList(),
            phoneNumbersMap = emptyMap(),
            cContactName = call.contactName?.takeIf { it.isNotBlank() },
            cCallType = call.getCallTypeString(),
            cGeocodedLocation = call.geocodedLocation?.takeIf { it.isNotBlank() },
            cCountryIso = call.countryIso?.takeIf { it.isNotBlank() },
            cPhoneAccountId = call.phoneAccountId?.takeIf { it.isNotBlank() },
            cUserPhone = configManager.phoneNumber?.takeIf { it.isNotBlank() },
            parentName = null,
            accountName = null,
            usersIds = emptyList(),
            usersNames = emptyMap(),
            usersColumns = emptyMap(),
            contactsIds = emptyList(),
            contactsNames = emptyMap(),
            contactsColumns = emptyMap(),
            leadsIds = emptyList(),
            leadsNames = emptyMap(),
            leadsColumns = emptyMap()
        )
    }

    private fun broadcast(status: String, message: String) {
        val intent = Intent("com.example.calllogger.API_RESPONSE")
        intent.putExtra("status", status)
        intent.putExtra("message", message)
        intent.putExtra("timestamp", System.currentTimeMillis())
        context.sendBroadcast(intent)
    }
}
