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
import com.example.calllogger.CallLoggerApplication
import com.example.calllogger.MainActivity
import com.example.calllogger.R
import com.example.calllogger.data.AppDatabase
import com.example.calllogger.data.CallLogEntity
import com.example.calllogger.network.EspoApiService
import com.example.calllogger.network.EspoCallRequest
import com.example.calllogger.network.RetrofitClient
import com.example.calllogger.util.ConfigManager
import com.example.calllogger.util.PermissionUtil
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class CallLogService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var database: AppDatabase
    private lateinit var configManager: ConfigManager
    private var espoApiService: EspoApiService? = null
    private var actualEspoUrl: String? = null // Store the actual URL being used
    
    companion object {
        private const val TAG = "CallLogService"
        private const val NOTIFICATION_ID = 1001
        private const val SYNC_INTERVAL = 30000L // 30 seconds
    }
    
    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        configManager = ConfigManager.getInstance(this)
        initializeEspoApi()
        Log.d(TAG, "CallLogService created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "CallLogService started")
        
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        
        // Check if this is a force sync request
        val forceSync = intent?.getBooleanExtra("force_sync", false) ?: false
        
        if (forceSync) {
            Log.d(TAG, "Force sync requested")
            serviceScope.launch {
                if (configManager.isSyncEnabled) {
                    syncUnsyncedCallLogs()
                } else {
                    broadcastApiResponse("⚠️ Sync Disabled", "Enable sync toggle to test sync")
                }
            }
        }
        
        // Start monitoring call logs
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
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
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
        
        Log.d(TAG, "Initializing ESPO API...")
        Log.d(TAG, "Base URL from config: '$baseUrl'")
        Log.d(TAG, "API Key present: ${!apiKey.isNullOrBlank()}")
        
        if (!baseUrl.isNullOrBlank() && !apiKey.isNullOrBlank()) {
            try {
                // Ensure URL ends with / (Retrofit requires trailing slash for base URL)
                // The endpoint "Call" will be appended by Retrofit
                val finalUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
                
                actualEspoUrl = "${finalUrl}Call" // Store actual endpoint URL for logging
                Log.d(TAG, "Base URL for Retrofit: '$finalUrl'")
                Log.d(TAG, "Actual endpoint will be: '$actualEspoUrl'")
                
                espoApiService = RetrofitClient.getInstance().createEspoApiService(finalUrl, apiKey)
                Log.d(TAG, "✅ ESPO API initialized successfully")
                broadcastApiResponse("✅ API Initialized", "ESPO Endpoint: $actualEspoUrl\nAPI Key: ${apiKey.take(10)}...\n\nReady to sync!")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Failed to initialize ESPO API", e)
                broadcastApiResponse("❌ API Init Failed", "Error: ${e.message}\n\nURL: $baseUrl\nException: ${e.javaClass.simpleName}\n\nStack:\n${e.stackTraceToString().take(300)}")
                espoApiService = null
                actualEspoUrl = null
            }
        } else {
            Log.d(TAG, "ESPO API not configured - sync will be disabled")
            if (baseUrl.isNullOrBlank()) {
                Log.d(TAG, "Missing: Base URL")
            }
            if (apiKey.isNullOrBlank()) {
                Log.d(TAG, "Missing: API Key")
            }
            espoApiService = null
            actualEspoUrl = null
        }
    }
    
    private fun startCallLogMonitoring() {
        serviceScope.launch {
            while (isActive) {
                try {
                    readAndStoreCallLogs()
                    if (configManager.isSyncEnabled) {
                        syncUnsyncedCallLogs()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error in call log monitoring", e)
                }
                delay(SYNC_INTERVAL)
            }
        }
    }
    
    private suspend fun readAndStoreCallLogs() {
        try {
            Log.d(TAG, "Starting to read call logs...")
            
            if (!PermissionUtil.hasCallLogPermission(this)) {
                Log.w(TAG, "No call log permission")
                return
            }

            val cursor: Cursor? = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(
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
                null,
                null,
                "${CallLog.Calls.DATE} DESC"
            )
            
            cursor?.use {
                var count = 0
                while (it.moveToNext() && count < 100) { // Limit in code instead of SQL
                    try {
                        val phoneNumber = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                        if (phoneNumber.isNullOrBlank()) continue
                        
                        // Core fields
                        val callType = it.getInt(it.getColumnIndexOrThrow(CallLog.Calls.TYPE))
                        val duration = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                        val timestamp = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DATE))
                        val cachedName = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME))
                        
                        // Additional fields
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
                        
                        // Check if this call log already exists
                        val existingCall = database.callLogDao().getCallLogByNumberAndTime(phoneNumber, timestamp)
                        if (existingCall == null) {
                            val contactName = cachedName ?: getContactName(phoneNumber)
                            
                            val callLogEntity = CallLogEntity(
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
                            
                            val insertedId = database.callLogDao().insertCallLog(callLogEntity)
                            if (insertedId > 0) {
                                count++
                                Log.d(TAG, "Stored new call log: $phoneNumber at $timestamp with ${callLogEntity.getCallFeaturesString()} features")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing call log entry", e)
                    }
                }
                Log.d(TAG, "Processed $count new call logs")
            } ?: run {
                Log.w(TAG, "Call log cursor is null")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading call logs", e)
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
                if (it.moveToFirst()) {
                    return it.getString(it.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME))
                }
            }
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error getting contact name for $phoneNumber", e)
            null
        }
    }
    
    private suspend fun syncUnsyncedCallLogs() {
        // Only sync if ESPO is properly configured
        if (!configManager.isEspoConfigured()) {
            Log.d(TAG, "ESPO not configured, skipping sync")
            broadcastApiResponse("❌ ESPO not configured", "Configure ESPO URL and API Key first")
            return
        }
        
        val espoApi = espoApiService ?: run {
            broadcastApiResponse("❌ API Service Error", "ESPO API service not initialized")
            return
        }
        
        val unsyncedCalls = database.callLogDao().getUnsyncedCallLogs()
        
        Log.d(TAG, "Found ${unsyncedCalls.size} unsynced calls")
        
        if (unsyncedCalls.isEmpty()) {
            broadcastApiResponse("✅ All Synced", "No pending calls to sync")
            return
        }
        
        for (call in unsyncedCalls) {
            try {
                val espoRequest = createEspoCallRequest(call)
                
                // Create Gson for pretty JSON logging
                val gson = GsonBuilder().setPrettyPrinting().create()
                val requestJson = gson.toJson(espoRequest)
                
                // Log COMPLETE request
                val requestLog = buildString {
                    appendLine("📤 SENDING TO ESPO")
                    appendLine("═".repeat(50))
                    appendLine("🌐 URL: ${actualEspoUrl ?: configManager.espoBaseUrl}")
                    appendLine()
                    appendLine("📋 COMPLETE REQUEST JSON:")
                    appendLine(requestJson)
                    appendLine()
                    appendLine("📞 Call: ${call.phoneNumber} (${call.contactName ?: "Unknown"})")
                }
                Log.d(TAG, requestLog)
                broadcastApiResponse("📤 Syncing...", requestLog)
                
                delay(500) // Ensure UI receives the broadcast
                
                val response = espoApi.createCall(espoRequest)
                
                if (response.isSuccessful) {
                    val espoCall = response.body()
                    if (espoCall != null) {
                        database.callLogDao().markAsSynced(call.id, espoCall.id)
                        
                        val responseJson = gson.toJson(espoCall)
                        
                        val successLog = buildString {
                            appendLine("✅ SYNC SUCCESS!")
                            appendLine("═".repeat(50))
                            appendLine("📊 HTTP: ${response.code()} ${response.message()}")
                            appendLine()
                            appendLine("📤 WHAT WE SENT:")
                            appendLine(requestJson)
                            appendLine()
                            appendLine("📥 WHAT ESPO RETURNED:")
                            appendLine(responseJson)
                            appendLine()
                            appendLine("🔍 KEY FIELD COMPARISON:")
                            appendLine("─".repeat(50))
                            appendLine("name: ${espoRequest.name}")
                            appendLine("  →  ${espoCall.name}")
                            if (espoCall.name != espoRequest.name) appendLine("  ⚠️ CHANGED!")
                            appendLine()
                            appendLine("status: ${espoRequest.status}")
                            appendLine("  →  ${espoCall.status}")
                            if (espoCall.status != espoRequest.status) appendLine("  ⚠️ CHANGED!")
                            appendLine()
                            appendLine("direction: ${espoRequest.direction}")
                            appendLine("  →  ${espoCall.direction}")
                            if (espoCall.direction != espoRequest.direction) appendLine("  ⚠️ CHANGED!")
                            appendLine()
                            appendLine("phoneNumbersMap: ${espoRequest.phoneNumbersMap}")
                            appendLine("  →  ${espoCall.phoneNumbersMap}")
                            if (espoCall.phoneNumbersMap != espoRequest.phoneNumbersMap) appendLine("  ⚠️ CHANGED!")
                            appendLine()
                            appendLine("📌 ESPO ID: ${espoCall.id}")
                            appendLine("📅 Created: ${espoCall.createdAt}")
                            appendLine()
                            appendLine("💡 NOTE: Custom fields (c*), phone, and")
                            appendLine("description are stored but not returned")
                            appendLine("in API response. Check ESPO admin UI to")
                            appendLine("verify they're saved.")
                            appendLine()
                            if (espoCall.name != espoRequest.name || 
                                espoCall.status != espoRequest.status || 
                                espoCall.direction != espoRequest.direction ||
                                espoCall.phoneNumbersMap != espoRequest.phoneNumbersMap) {
                                appendLine("⚠️ ESPO MODIFIED DATA!")
                                appendLine("Check: Admin → Workflows/Formula")
                            }
                        }
                        
                        Log.d(TAG, successLog)
                        broadcastApiResponse("✅ Success", successLog)
                        delay(1000)
                    } else {
                        val errorLog = "⚠️ Empty response\nHTTP: ${response.code()}"
                        Log.w(TAG, errorLog)
                        broadcastApiResponse("⚠️ Empty", errorLog)
                        delay(1000)
                    }
                } else {
                    database.callLogDao().incrementSyncAttempts(call.id, System.currentTimeMillis())
                    
                    val errorBody = response.errorBody()?.string() ?: "No error details"
                    val errorLog = buildString {
                        appendLine("❌ SYNC FAILED")
                        appendLine("═".repeat(50))
                        appendLine("📊 HTTP: ${response.code()} ${response.message()}")
                        appendLine()
                        appendLine("🔴 ERROR:")
                        appendLine(errorBody)
                        appendLine()
                        appendLine("📤 REQUEST:")
                        appendLine(requestJson)
                    }
                    
                    Log.e(TAG, errorLog)
                    broadcastApiResponse("❌ Failed", errorLog)
                    delay(1000)
                }
            } catch (e: Exception) {
                database.callLogDao().incrementSyncAttempts(call.id, System.currentTimeMillis())
                
                val exceptionLog = buildString {
                    appendLine("❌ EXCEPTION")
                    appendLine("═".repeat(50))
                    appendLine("Type: ${e.javaClass.simpleName}")
                    appendLine("Message: ${e.message}")
                    appendLine()
                    appendLine("Stack:")
                    appendLine(e.stackTraceToString())
                }
                
                Log.e(TAG, exceptionLog, e)
                broadcastApiResponse("❌ Exception", exceptionLog)
                delay(1000)
            }
        }
        
        configManager.lastSyncTime = System.currentTimeMillis()
    }
    
    private fun broadcastApiResponse(status: String, message: String) {
        val intent = Intent("com.example.calllogger.API_RESPONSE")
        intent.putExtra("status", status)
        intent.putExtra("message", message)
        intent.putExtra("timestamp", System.currentTimeMillis())
        sendBroadcast(intent)
    }
    
    private fun createEspoCallRequest(call: CallLogEntity): EspoCallRequest {
        // Format the call start date/time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val callStartTime = dateFormat.format(Date(call.timestamp))
        
        // Determine direction based on call type
        val direction = when (call.callType) {
            CallLogEntity.INCOMING_TYPE -> "Inbound"
            CallLogEntity.OUTGOING_TYPE -> "Outbound"
            CallLogEntity.MISSED_TYPE -> "Inbound" // Missed calls are inbound
            CallLogEntity.REJECTED_TYPE -> "Inbound" // Rejected calls are inbound
            CallLogEntity.BLOCKED_TYPE -> "Inbound" // Blocked calls are inbound
            else -> "Inbound"
        }
        
        // Determine status based on duration and call type
        val status = when {
            call.duration > 0 -> "Held" // Call was answered
            call.callType == CallLogEntity.MISSED_TYPE -> "Not Held" // Missed call
            call.callType == CallLogEntity.REJECTED_TYPE -> "Not Held" // Rejected call
            call.callType == CallLogEntity.BLOCKED_TYPE -> "Not Held" // Blocked call
            else -> "Planned" // Default
        }
        
        // Create a descriptive name for the call
        val callName = buildString {
            append(call.getCallTypeString())
            append(" call")
            if (!call.contactName.isNullOrBlank()) {
                append(" with ${call.contactName}")
            } else {
                append(" - ${call.phoneNumber}")
            }
        }
        
        // Get user's own phone number for cUserPhone field (already formatted with country code)
        val ownPhoneNumber = configManager.phoneNumber
        
        // Return request with all fields matching your API structure
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
            
            // Additional fields with 'c' prefix to match ESPO schema
            cContactName = call.contactName?.takeIf { it.isNotBlank() },
            cCallType = call.getCallTypeString(),
            cGeocodedLocation = call.geocodedLocation?.takeIf { it.isNotBlank() },
            cCountryIso = call.countryIso?.takeIf { it.isNotBlank() },
            cPhoneAccountId = call.phoneAccountId?.takeIf { it.isNotBlank() },
            cUserPhone = ownPhoneNumber?.takeIf { it.isNotBlank() },
            
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
}