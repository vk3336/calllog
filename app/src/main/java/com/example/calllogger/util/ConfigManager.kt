package com.example.calllogger.util

import android.content.Context
import android.content.SharedPreferences
import android.telephony.TelephonyManager
import android.util.Log

class ConfigManager private constructor(private val context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("call_logger_config", Context.MODE_PRIVATE)
    
    companion object {
        @Volatile
        private var INSTANCE: ConfigManager? = null
        
        private const val TAG = "ConfigManager"
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val KEY_ESPO_BASE_URL = "espo_base_url"
        private const val KEY_ESPO_API_KEY = "espo_api_key"
        private const val KEY_SYNC_ENABLED = "sync_enabled"
        private const val KEY_LAST_SYNC_TIME = "last_sync_time"
        private const val KEY_SIM1_NAME = "sim1_name"
        private const val KEY_SIM2_NAME = "sim2_name"
        
        fun getInstance(context: Context): ConfigManager {
            return INSTANCE ?: synchronized(this) {
                val instance = ConfigManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
    
    var phoneNumber: String?
        get() {
            // Try to get cached phone number first
            val cached = sharedPreferences.getString(KEY_PHONE_NUMBER, null)
            if (!cached.isNullOrBlank()) {
                return cached
            }
            
            // Auto-detect device phone number
            val detected = getDevicePhoneNumber()
            if (!detected.isNullOrBlank()) {
                // Cache it for future use
                phoneNumber = detected
                return detected
            }
            
            return null
        }
        set(value) = sharedPreferences.edit().putString(KEY_PHONE_NUMBER, value).apply()
    
    /**
     * Automatically detect device phone number from SIM card
     * Returns number in international format with country code (e.g., +919876543210)
     */
    private fun getDevicePhoneNumber(): String? {
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            if (telephonyManager == null) {
                Log.w(TAG, "TelephonyManager not available")
                return null
            }
            
            // Try to get phone number from SIM
            val rawNumber = telephonyManager.line1Number
            if (!rawNumber.isNullOrBlank()) {
                Log.d(TAG, "Detected phone number from SIM: $rawNumber")
                
                // Format with country code if not already present
                return formatPhoneNumberWithCountryCode(rawNumber, telephonyManager.networkCountryIso)
            }
            
            Log.w(TAG, "No phone number available from SIM")
            return null
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied to read phone number", e)
            return null
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting phone number", e)
            return null
        }
    }
    
    /**
     * Format phone number with country code
     * Ensures number starts with + and country code
     */
    private fun formatPhoneNumberWithCountryCode(number: String, countryIso: String?): String {
        var formatted = number.trim()
        
        // If already has +, return as is
        if (formatted.startsWith("+")) {
            return formatted
        }
        
        // Get country code based on ISO
        val countryCode = when (countryIso?.uppercase()) {
            "IN" -> "+91"
            "US", "CA" -> "+1"
            "GB" -> "+44"
            "AU" -> "+61"
            "DE" -> "+49"
            "FR" -> "+33"
            "JP" -> "+81"
            "CN" -> "+86"
            "BR" -> "+55"
            "ZA" -> "+27"
            else -> {
                Log.w(TAG, "Unknown country ISO: $countryIso, defaulting to +91")
                "+91"
            }
        }
        
        // Remove leading 0 if present (common in some countries)
        if (formatted.startsWith("0")) {
            formatted = formatted.substring(1)
        }
        
        return "$countryCode$formatted"
    }
    
    var espoBaseUrl: String?
        get() = sharedPreferences.getString(KEY_ESPO_BASE_URL, null)
        set(value) = sharedPreferences.edit().putString(KEY_ESPO_BASE_URL, value).apply()
    
    var espoApiKey: String?
        get() = sharedPreferences.getString(KEY_ESPO_API_KEY, null)
        set(value) = sharedPreferences.edit().putString(KEY_ESPO_API_KEY, value).apply()
    
    var isSyncEnabled: Boolean
        get() = sharedPreferences.getBoolean(KEY_SYNC_ENABLED, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_SYNC_ENABLED, value).apply()
    
    var lastSyncTime: Long
        get() = sharedPreferences.getLong(KEY_LAST_SYNC_TIME, 0)
        set(value) = sharedPreferences.edit().putLong(KEY_LAST_SYNC_TIME, value).apply()
    
    var sim1Name: String?
        get() = sharedPreferences.getString(KEY_SIM1_NAME, "Personal SIM")
        set(value) = sharedPreferences.edit().putString(KEY_SIM1_NAME, value).apply()
    
    var sim2Name: String?
        get() = sharedPreferences.getString(KEY_SIM2_NAME, "Work SIM")
        set(value) = sharedPreferences.edit().putString(KEY_SIM2_NAME, value).apply()
    
    fun isConfigured(): Boolean {
        // Phone number is now auto-detected, so always considered configured
        return true
    }
    
    fun isEspoConfigured(): Boolean {
        return !espoBaseUrl.isNullOrBlank() && 
               !espoApiKey.isNullOrBlank()
    }
    
    fun clearConfig() {
        sharedPreferences.edit().clear().apply()
    }
}