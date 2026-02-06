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
                
                // Log the request being sent
                val requestLog = buildString {
                    appendLine("📤 Sending to ESPO:")
                    appendLine("URL: ${actualEspoUrl ?: configManager.espoBaseUrl}")
                    appendLine("Phone: ${call.phoneNumber}")
                    appendLine("Type: ${call.getCallTypeString()}")
                    appendLine("Duration: ${call.duration}s")
                    appendLine("Time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date(call.timestamp))}")
                }
                Log.d(TAG, requestLog)
                broadcastApiResponse("📤 Syncing...", requestLog)
                
                val response = espoApi.createCall(espoRequest)
                
                if (response.isSuccessful) {
                    val espoCall = response.body()
                    if (espoCall != null) {
                        database.callLogDao().markAsSynced(call.id, espoCall.id)
                        
                        val successLog = buildString {
                            appendLine("✅ SUCCESS!")
                            appendLine("Status: ${response.code()} ${response.message()}")
                            appendLine("ESPO ID: ${espoCall.id}")
                            appendLine("Call Name: ${espoCall.name}")
                            appendLine("Direction: ${espoCall.direction}")
                            appendLine("Status: ${espoCall.status}")
                            appendLine("Duration: ${espoCall.duration}s")
                            appendLine("Created: ${espoCall.createdAt}")
                            appendLine("\nPhone: ${call.phoneNumber}")
                            appendLine("Contact: ${call.contactName ?: "Unknown"}")
                        }
                        
                        Log.d(TAG, "Successfully synced call: ${call.phoneNumber}")
                        Log.d(TAG, successLog)
                        broadcastApiResponse("✅ Sync Success", successLog)
                    } else {
                        val errorLog = "⚠️ Response body is null\nStatus: ${response.code()}"
                        Log.w(TAG, errorLog)
                        broadcastApiResponse("⚠️ Empty Response", errorLog)
                    }
                } else {
                    database.callLogDao().incrementSyncAttempts(call.id, System.currentTimeMillis())
                    
                    val errorBody = response.errorBody()?.string() ?: "No error details"
                    val errorLog = buildString {
                        appendLine("❌ SYNC FAILED")
                        appendLine("Status: ${response.code()} ${response.message()}")
                        appendLine("Phone: ${call.phoneNumber}")
                        appendLine("Error Details:")
                        appendLine(errorBody)
                        appendLine("\nRequest Data:")
                        appendLine("Name: ${espoRequest.name}")
                        appendLine("Direction: ${espoRequest.direction}")
                        appendLine("Status: ${espoRequest.status}")
                        appendLine("Duration: ${espoRequest.duration}s")
                    }
                    
                    Log.e(TAG, "Failed to sync call: ${response.code()} - ${response.message()}")
                    Log.e(TAG, "Error body: $errorBody")
                    broadcastApiResponse("❌ Sync Failed", errorLog)
                }
            } catch (e: Exception) {
                database.callLogDao().incrementSyncAttempts(call.id, System.currentTimeMillis())
                
                val exceptionLog = buildString {
                    appendLine("❌ EXCEPTION")
                    appendLine("Phone: ${call.phoneNumber}")
                    appendLine("Error: ${e.javaClass.simpleName}")
                    appendLine("Message: ${e.message}")
                    appendLine("\nStack trace:")
                    appendLine(e.stackTraceToString().take(500))
                }
                
                Log.e(TAG, "Exception syncing call: ${call.phoneNumber}", e)
                broadcastApiResponse("❌ Exception", exceptionLog)
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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val callDate = dateFormat.format(Date(call.timestamp))
        
        val direction = when (call.callType) {
            CallLogEntity.INCOMING_TYPE -> "Inbound"
            CallLogEntity.OUTGOING_TYPE -> "Outbound"
            else -> "Inbound" // Default for missed calls
        }
        
        val status = when {
            call.duration > 0 -> "Held"
            call.callType == CallLogEntity.MISSED_TYPE -> "Not Held"
            else -> "Planned"
        }
        
        val callName = buildString {
            append(call.getCallTypeString())
            append(" call")
            if (!call.contactName.isNullOrBlank()) {
                append(" with ${call.contactName}")
            } else {
                append(" - ${call.phoneNumber}")
            }
        }
        
        // Create minimal request matching Postman success
        return EspoCallRequest(
            name = callName,
            deleted = false,
            status = status,
            duration = call.duration.toInt(),
            reminders = emptyList(),
            direction = direction,
            createdAt = callDate,
            modifiedAt = callDate,
            phoneNumbersMap = emptyMap(), // Empty map like Postman
            parentName = call.contactName,
            accountName = null,
            usersIds = emptyList(),
            usersNames = emptyMap(),
            usersColumns = emptyMap(),
            contactsIds = emptyList(),
            contactsNames = emptyMap(),
            contactsColumns = emptyMap(),
            leadsIds = emptyList(),
            leadsNames = emptyMap(),
            leadsColumns = emptyMap(),
            teamsIds = emptyList(),
            teamsNames = emptyMap()
        )
    }
}