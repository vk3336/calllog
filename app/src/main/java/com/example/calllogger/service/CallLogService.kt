package com.example.calllogger.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.database.Cursor
import android.os.IBinder
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.calllogger.CallLoggerApplication
import com.example.calllogger.MainActivity
import com.example.calllogger.R
import com.example.calllogger.data.AppDatabase
import com.example.calllogger.data.CallLogEntity
import com.example.calllogger.network.EspoApiService
import com.example.calllogger.network.RetrofitClient
import com.example.calllogger.util.ConfigManager
import com.example.calllogger.util.PermissionUtil
import com.example.calllogger.worker.SyncWorker
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class CallLogService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var database: AppDatabase
    private lateinit var configManager: ConfigManager
    private lateinit var syncManager: EspoSyncManager
    private var espoApiService: EspoApiService? = null
    private var actualEspoUrl: String? = null

    companion object {
        private const val TAG = "CallLogService"
        private const val NOTIFICATION_ID = 1001
        // Sync interval: every 1 hour (WorkManager handles this, not a loop)
        private const val SYNC_INTERVAL_HOURS = 1L
    }

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        configManager = ConfigManager.getInstance(this)
        syncManager = EspoSyncManager(this)
        initializeEspoApi()
        scheduleSyncWorker()
        Log.d(TAG, "CallLogService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "CallLogService started")
        startForeground(NOTIFICATION_ID, createNotification())

        val forceSync = intent?.getBooleanExtra("force_sync", false) ?: false

        serviceScope.launch {
            // Reset any stuck IN_PROGRESS rows from a previous crash
            database.callLogDao().resetStuckInProgressRows()

            if (forceSync) {
                Log.d(TAG, "Force sync requested")
                if (configManager.isSyncEnabled) {
                    espoApiService?.let { syncManager.syncPendingCalls(it) }
                }
            }
        }

        startCallLogMonitoring()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        Log.d(TAG, "CallLogService destroyed")
    }

    private fun createNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CallLoggerApplication.CHANNEL_ID)
            .setContentTitle("Call Logger Active")
            .setContentText("Monitoring and syncing call logs")
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun initializeEspoApi() {
        val baseUrl = configManager.espoBaseUrl
        val apiKey = configManager.espoApiKey

        if (!baseUrl.isNullOrBlank() && !apiKey.isNullOrBlank()) {
            try {
                val finalUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
                actualEspoUrl = "${finalUrl}Call"
                espoApiService = RetrofitClient.getInstance().createEspoApiService(finalUrl, apiKey)
                Log.d(TAG, "✅ ESPO API initialized: $actualEspoUrl")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Failed to initialize ESPO API", e)
                espoApiService = null
                actualEspoUrl = null
            }
        } else {
            espoApiService = null
            actualEspoUrl = null
        }
    }

    private fun startCallLogMonitoring() {
        serviceScope.launch {
            try {
                // Read and store any new call logs from the Android system
                readAndStoreCallLogs()
                Log.d(TAG, "Initial call log read complete")
            } catch (e: Exception) {
                Log.e(TAG, "Error reading call logs on start", e)
            }
            // Periodic sync is now handled by WorkManager (every 1 hour)
            // This service only does the initial read on start
        }
    }

    /**
     * Schedules a periodic WorkManager job to sync pending calls to EspoCRM.
     * Runs every SYNC_INTERVAL_HOURS hours.
     * Uses KEEP policy — if already scheduled, the existing schedule is preserved.
     * Requires network connectivity before running.
     */
    private fun scheduleSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            SYNC_INTERVAL_HOURS, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // Don't reset timer if already scheduled
            syncRequest
        )

        Log.d(TAG, "✅ SyncWorker scheduled: every ${SYNC_INTERVAL_HOURS}h (requires network)")
    }

    private suspend fun readAndStoreCallLogs() {
        if (!PermissionUtil.hasCallLogPermission(this)) {
            Log.w(TAG, "No call log permission")
            return
        }

        val cursor: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION,
                CallLog.Calls.DATE,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.GEOCODED_LOCATION,
                CallLog.Calls.PHONE_ACCOUNT_ID,
                CallLog.Calls.VIA_NUMBER,
                CallLog.Calls.TRANSCRIPTION,
                CallLog.Calls.IS_READ,
                CallLog.Calls.NEW,
                CallLog.Calls.COUNTRY_ISO,
                CallLog.Calls.DATA_USAGE,
                CallLog.Calls.FEATURES,
                CallLog.Calls.NUMBER_PRESENTATION,
                CallLog.Calls.POST_DIAL_DIGITS
            ),
            null, null,
            "${CallLog.Calls.DATE} DESC"
        )

        cursor?.use {
            var count = 0
            while (it.moveToNext() && count < 100) {
                try {
                    val systemCallId = it.getString(it.getColumnIndexOrThrow(CallLog.Calls._ID))
                    val phoneNumber = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    if (phoneNumber.isNullOrBlank()) continue

                    // Skip if already in DB (UNIQUE index on systemCallId handles this,
                    // but explicit check avoids unnecessary contact lookup)
                    if (database.callLogDao().getCallLogBySystemId(systemCallId) != null) continue

                    val callType = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.TYPE))
                    val duration = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                    val timestamp = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DATE))
                    val cachedName = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME))
                    val geocodedLocation = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.GEOCODED_LOCATION))
                    val phoneAccountId = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.PHONE_ACCOUNT_ID))
                    val viaNumber = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.VIA_NUMBER))
                    val transcription = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.TRANSCRIPTION))
                    val isRead = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.IS_READ))
                    val isNew = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.NEW))
                    val countryIso = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.COUNTRY_ISO))
                    val dataUsage = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DATA_USAGE))
                    val features = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.FEATURES))
                    val numberPresentation = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.NUMBER_PRESENTATION))
                    val postDialDigits = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.POST_DIAL_DIGITS))

                    val contactName = cachedName ?: getContactName(phoneNumber)

                    val entity = CallLogEntity(
                        systemCallId = systemCallId,
                        phoneNumber = phoneNumber,
                        contactName = contactName,
                        callType = callType,
                        duration = duration,
                        timestamp = timestamp,
                        geocodedLocation = geocodedLocation,
                        phoneAccountId = phoneAccountId,
                        viaNumber = viaNumber,
                        voicemailTranscription = transcription,
                        isRead = isRead,
                        isNew = isNew,
                        countryIso = countryIso,
                        dataUsage = if (dataUsage == 0L) null else dataUsage,
                        features = features,
                        numberPresentation = numberPresentation,
                        postDialDigits = postDialDigits
                    )

                    val insertedId = database.callLogDao().insertCallLog(entity)
                    if (insertedId > 0) {
                        count++
                        Log.d(TAG, "Stored new call: $phoneNumber systemId=$systemCallId")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing call log entry", e)
                }
            }
            Log.d(TAG, "Processed $count new call logs")
        }
    }

    private fun getContactName(phoneNumber: String): String? {
        return try {
            val cursor = contentResolver.query(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon()
                    .appendPath(phoneNumber).build(),
                arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
                null, null, null
            )
            cursor?.use {
                if (it.moveToFirst()) it.getString(it.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME))
                else null
            }
        } catch (e: Exception) {
            null
        }
    }
}
