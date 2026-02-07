package com.example.calllogger

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calllogger.adapter.CallLogAdapter
import com.example.calllogger.data.AppDatabase
import com.example.calllogger.databinding.ActivityMainBinding
import com.example.calllogger.service.CallLogService
import com.example.calllogger.util.ConfigManager
import com.example.calllogger.util.PermissionUtil
import kotlinx.coroutines.launch
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var configManager: ConfigManager
    private lateinit var database: AppDatabase
    private lateinit var callLogAdapter: CallLogAdapter
    private var apiResponseReceiver: BroadcastReceiver? = null
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        configManager = ConfigManager.getInstance(this)
        database = AppDatabase.getDatabase(this)
        
        setupUI()
        setupApiResponseReceiver()
        checkPermissionsAndStart()
        
        // Immediately try to read call logs if permissions are available
        if (PermissionUtil.hasAllPermissions(this)) {
            loadCallLogsImmediately()
        }
    }
    
    private fun setupUI() {
        // Setup RecyclerView
        callLogAdapter = CallLogAdapter()
        binding.recyclerViewCallLogs.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = callLogAdapter
        }
        
        // Load saved configuration
        loadConfiguration()
        
        // Setup click listeners
        binding.buttonSaveConfig.setOnClickListener {
            saveConfiguration()
        }
        
        binding.buttonStopService.setOnClickListener {
            stopCallLogService()
        }
        
        // Add a manual refresh button for testing
        binding.buttonSaveConfig.setOnLongClickListener {
            if (PermissionUtil.hasCallLogPermission(this)) {
                Toast.makeText(this, "Reading call logs...", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    readCallLogsDirectly()
                }
            } else {
                Toast.makeText(this, "Call log permission required", Toast.LENGTH_SHORT).show()
                PermissionUtil.requestAllPermissions(this)
            }
            true
        }
        
        // Add click listener to start service button to also read call logs
        binding.buttonStartService.setOnClickListener {
            startCallLogService()
            if (PermissionUtil.hasCallLogPermission(this)) {
                lifecycleScope.launch {
                    readCallLogsDirectly()
                }
            }
        }
        
        binding.switchSyncEnabled.setOnCheckedChangeListener { _, isChecked ->
            configManager.isSyncEnabled = isChecked
            updateSyncStatus()
        }
        
        // Test sync button
        binding.buttonTestSync.setOnClickListener {
            testSyncNow()
        }
        
        // Clear API response button
        binding.buttonClearResponse.setOnClickListener {
            clearApiResponse()
        }
        
        // Observe call logs
        observeCallLogs()
        updateStats()
    }
    
    private fun setupApiResponseReceiver() {
        apiResponseReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "com.example.calllogger.API_RESPONSE") {
                    val status = intent.getStringExtra("status") ?: "Unknown"
                    val message = intent.getStringExtra("message") ?: "No message"
                    val timestamp = intent.getLongExtra("timestamp", System.currentTimeMillis())
                    
                    updateApiResponse(status, message, timestamp)
                }
            }
        }
        
        val filter = IntentFilter("com.example.calllogger.API_RESPONSE")
        registerReceiver(apiResponseReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }
    
    private fun updateApiResponse(status: String, message: String, timestamp: Long) {
        runOnUiThread {
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val timeStr = timeFormat.format(Date(timestamp))
            
            val fullMessage = buildString {
                appendLine("[$timeStr] $status")
                appendLine("─".repeat(40))
                appendLine(message)
            }
            
            binding.textViewApiResponse.text = fullMessage
            binding.textViewApiStatus.text = "Status: $status"
            
            // Color code the status
            when {
                status.contains("✅") || status.contains("SUCCESS", ignoreCase = true) -> {
                    binding.textViewApiStatus.setTextColor(Color.parseColor("#4CAF50"))
                }
                status.contains("❌") || status.contains("FAIL", ignoreCase = true) || status.contains("ERROR", ignoreCase = true) -> {
                    binding.textViewApiStatus.setTextColor(Color.parseColor("#F44336"))
                }
                status.contains("📤") || status.contains("Syncing", ignoreCase = true) -> {
                    binding.textViewApiStatus.setTextColor(Color.parseColor("#2196F3"))
                }
                else -> {
                    binding.textViewApiStatus.setTextColor(Color.parseColor("#FF9800"))
                }
            }
            
            Log.d(TAG, "API Response updated: $status")
        }
    }
    
    private fun clearApiResponse() {
        binding.textViewApiResponse.text = "Response cleared. Waiting for next sync..."
        binding.textViewApiStatus.text = "Status: Idle"
        binding.textViewApiStatus.setTextColor(Color.parseColor("#757575"))
        Toast.makeText(this, "API response cleared", Toast.LENGTH_SHORT).show()
    }
    
    private fun testSyncNow() {
        if (!configManager.isEspoConfigured()) {
            Toast.makeText(this, "Please configure ESPO first", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (!configManager.isSyncEnabled) {
            Toast.makeText(this, "Please enable sync first", Toast.LENGTH_SHORT).show()
            return
        }
        
        lifecycleScope.launch {
            val unsyncedCount = database.callLogDao().getCallLogCount() - database.callLogDao().getSyncedCallLogCount()
            
            if (unsyncedCount == 0) {
                Toast.makeText(this@MainActivity, "No unsynced calls to test", Toast.LENGTH_SHORT).show()
                updateApiResponse("ℹ️ No Data", "All calls are already synced.\n\nMake a test call or wait for new calls.", System.currentTimeMillis())
            } else {
                Toast.makeText(this@MainActivity, "Testing sync for $unsyncedCount call(s)...", Toast.LENGTH_SHORT).show()
                updateApiResponse("🔄 Testing", "Triggering manual sync for $unsyncedCount unsynced call(s)...", System.currentTimeMillis())
                
                // Trigger the service to sync immediately
                val serviceIntent = Intent(this@MainActivity, CallLogService::class.java)
                serviceIntent.putExtra("force_sync", true)
                startForegroundService(serviceIntent)
            }
        }
    }
    
    private fun loadConfiguration() {
        binding.editTextEspoUrl.setText(configManager.espoBaseUrl ?: "")
        binding.editTextApiKey.setText(configManager.espoApiKey ?: "")
        binding.switchSyncEnabled.isChecked = configManager.isSyncEnabled
        
        updateSyncStatus()
        
        // Log detected phone number
        val detectedPhone = configManager.phoneNumber
        Log.d(TAG, "Auto-detected device phone number: ${detectedPhone ?: "Not available"}")
        if (!detectedPhone.isNullOrBlank()) {
            Toast.makeText(this, "Device number: $detectedPhone", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun saveConfiguration() {
        val espoUrl = binding.editTextEspoUrl.text.toString().trim()
        val apiKey = binding.editTextApiKey.text.toString().trim()
        
        // Save ESPO URL - remove trailing slash if present
        val cleanUrl = espoUrl.trimEnd('/')
        configManager.espoBaseUrl = cleanUrl
        
        // Save API key
        configManager.espoApiKey = apiKey
        
        // Get auto-detected phone number
        val phoneNumber = configManager.phoneNumber
        
        Log.d(TAG, "Configuration saved:")
        Log.d(TAG, "Device Phone (auto-detected): ${phoneNumber ?: "Not available"}")
        Log.d(TAG, "ESPO URL: $cleanUrl")
        Log.d(TAG, "API Key: ${if (apiKey.isNotBlank()) "Present" else "Missing"}")
        
        val message = if (!phoneNumber.isNullOrBlank()) {
            "Configuration saved\nDevice: $phoneNumber"
        } else {
            "Configuration saved\n⚠️ Device number not detected"
        }
        
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        updateSyncStatus()
        
        // Restart service to reinitialize API with new config
        if (configManager.isEspoConfigured()) {
            Log.d(TAG, "ESPO is configured, restarting service...")
            stopCallLogService()
            binding.root.postDelayed({
                startCallLogService()
                Toast.makeText(this, "Service restarted", Toast.LENGTH_SHORT).show()
            }, 1000)
        } else {
            Log.d(TAG, "ESPO not fully configured yet")
        }
        
        // Immediately load call logs
        if (PermissionUtil.hasCallLogPermission(this)) {
            loadCallLogsImmediately()
        } else {
            PermissionUtil.requestAllPermissions(this)
        }
    }
    
    private fun updateSyncStatus() {
        val isEspoConfigured = configManager.isEspoConfigured()
        val isSyncEnabled = configManager.isSyncEnabled
        val phoneNumber = configManager.phoneNumber
        
        binding.textViewSyncStatus.text = when {
            phoneNumber.isNullOrBlank() -> "⚠️ Device number not detected"
            !isEspoConfigured -> "ESPO configuration incomplete"
            !isSyncEnabled -> "Sync disabled"
            else -> "✅ Sync enabled (Device: $phoneNumber)"
        }
        
        binding.switchSyncEnabled.isEnabled = isEspoConfigured
    }
    
    private fun checkPermissionsAndStart() {
        if (PermissionUtil.hasAllPermissions(this)) {
            startCallLogService()
        } else {
            PermissionUtil.requestAllPermissions(this)
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == PermissionUtil.PERMISSION_REQUEST_CODE) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            
            if (allGranted) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
                startCallLogService()
                // Load call logs immediately after permissions are granted
                loadCallLogsImmediately()
            } else {
                Toast.makeText(this, "Permissions required for app to work", Toast.LENGTH_LONG).show()
                
                val missingPermissions = PermissionUtil.getMissingPermissions(this)
                Log.w(TAG, "Missing permissions: $missingPermissions")
            }
        }
    }
    
    private fun startCallLogService() {
        if (!PermissionUtil.hasAllPermissions(this)) {
            Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
            return
        }
        
        try {
            val serviceIntent = Intent(this, CallLogService::class.java)
            startForegroundService(serviceIntent)
            
            binding.textViewServiceStatus.text = "Service: Running"
            Toast.makeText(this, "Call log service started", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "CallLogService started")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start service", e)
            Toast.makeText(this, "Failed to start service: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun stopCallLogService() {
        try {
            val serviceIntent = Intent(this, CallLogService::class.java)
            stopService(serviceIntent)
            
            binding.textViewServiceStatus.text = "Service: Stopped"
            Toast.makeText(this, "Call log service stopped", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "CallLogService stopped")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop service", e)
        }
    }
    
    private fun restartCallLogService() {
        stopCallLogService()
        // Small delay before restarting
        binding.root.postDelayed({
            startCallLogService()
        }, 1000)
    }
    
    private fun observeCallLogs() {
        lifecycleScope.launch {
            database.callLogDao().getAllCallLogs().collect { callLogs ->
                callLogAdapter.submitList(callLogs)
                updateStats()
            }
        }
    }
    
    private fun updateStats() {
        lifecycleScope.launch {
            val totalCount = database.callLogDao().getCallLogCount()
            val syncedCount = database.callLogDao().getSyncedCallLogCount()
            val pendingCount = totalCount - syncedCount
            
            // Update individual stat TextViews
            binding.textViewTotalCalls.text = totalCount.toString()
            binding.textViewSyncedCalls.text = syncedCount.toString()
            binding.textViewPendingCalls.text = pendingCount.toString()
            
            val lastSyncTime = configManager.lastSyncTime
            if (lastSyncTime > 0) {
                val lastSyncText = "Last sync: ${android.text.format.DateFormat.format("MMM dd, HH:mm", lastSyncTime)}"
                binding.textViewLastSync.text = lastSyncText
            } else {
                binding.textViewLastSync.text = "Never synced"
            }
        }
    }
    
    private fun loadCallLogsImmediately() {
        lifecycleScope.launch {
            try {
                // Trigger service to read call logs immediately
                val serviceIntent = Intent(this@MainActivity, CallLogService::class.java)
                startForegroundService(serviceIntent)
                
                // Also manually read some call logs to populate the UI quickly
                readCallLogsDirectly()
            } catch (e: Exception) {
                Log.e(TAG, "Error loading call logs immediately", e)
            }
        }
    }
    
    private suspend fun readCallLogsDirectly() {
        try {
            Log.d(TAG, "Reading call logs directly...")
            
            val cursor = contentResolver.query(
                android.provider.CallLog.Calls.CONTENT_URI,
                arrayOf(
                    android.provider.CallLog.Calls.NUMBER,
                    android.provider.CallLog.Calls.TYPE,
                    android.provider.CallLog.Calls.DURATION,
                    android.provider.CallLog.Calls.DATE,
                    android.provider.CallLog.Calls.CACHED_NAME,
                    android.provider.CallLog.Calls.GEOCODED_LOCATION,
                    android.provider.CallLog.Calls.PHONE_ACCOUNT_ID,
                    android.provider.CallLog.Calls.VIA_NUMBER,
                    android.provider.CallLog.Calls.TRANSCRIPTION,
                    android.provider.CallLog.Calls.IS_READ,
                    android.provider.CallLog.Calls.NEW,
                    android.provider.CallLog.Calls.COUNTRY_ISO,
                    android.provider.CallLog.Calls.DATA_USAGE,
                    android.provider.CallLog.Calls.FEATURES,
                    android.provider.CallLog.Calls.NUMBER_PRESENTATION,
                    android.provider.CallLog.Calls.POST_DIAL_DIGITS
                ),
                null,
                null,
                "${android.provider.CallLog.Calls.DATE} DESC"
            )
            
            cursor?.use {
                var count = 0
                while (it.moveToNext() && count < 50) { // Limit in code instead of SQL
                    try {
                        val phoneNumber = it.getString(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.NUMBER))
                        if (phoneNumber.isNullOrBlank()) continue
                        
                        // Core fields
                        val callType = it.getInt(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.TYPE))
                        val duration = it.getLong(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.DURATION))
                        val timestamp = it.getLong(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATE))
                        val cachedName = it.getString(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.CACHED_NAME))
                        
                        // Additional fields
                        val geocodedLocation = it.getString(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.GEOCODED_LOCATION))
                        val phoneAccountId = it.getString(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.PHONE_ACCOUNT_ID))
                        val viaNumber = it.getString(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.VIA_NUMBER))
                        val transcription = it.getString(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.TRANSCRIPTION))
                        val isRead = it.getInt(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.IS_READ))
                        val isNew = it.getInt(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.NEW))
                        val countryIso = it.getString(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.COUNTRY_ISO))
                        val dataUsage = it.getLong(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.DATA_USAGE))
                        val features = it.getInt(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.FEATURES))
                        val numberPresentation = it.getInt(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.NUMBER_PRESENTATION))
                        val postDialDigits = it.getString(it.getColumnIndexOrThrow(android.provider.CallLog.Calls.POST_DIAL_DIGITS))
                        
                        // Check if this call log already exists
                        val existingCall = database.callLogDao().getCallLogByNumberAndTime(phoneNumber, timestamp)
                        if (existingCall == null) {
                            val callLogEntity = com.example.calllogger.data.CallLogEntity(
                                phoneNumber = phoneNumber,
                                contactName = cachedName,
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
                                Log.d(TAG, "Inserted call log: $phoneNumber at $timestamp")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error processing call log entry", e)
                    }
                }
                Log.d(TAG, "Processed $count call logs")
                
                // Update UI on main thread
                runOnUiThread {
                    updateStats()
                    Toast.makeText(this@MainActivity, "Loaded $count call logs", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Log.w(TAG, "Call log cursor is null - no permission or no data")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "No call logs found or permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied for call logs", e)
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Call log permission required", Toast.LENGTH_LONG).show()
                PermissionUtil.requestAllPermissions(this@MainActivity)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading call logs directly", e)
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Error reading call logs: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        updateStats()
        
        // Refresh call logs when returning to the app
        if (PermissionUtil.hasAllPermissions(this)) {
            lifecycleScope.launch {
                readCallLogsDirectly()
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        apiResponseReceiver?.let {
            unregisterReceiver(it)
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}