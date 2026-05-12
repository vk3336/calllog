package com.example.calllogger.util

import android.content.Context
import android.content.SharedPreferences
import android.telephony.TelephonyManager
import android.util.Log
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

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
     * Automatically detect device phone number from SIM card.
     * Returns number in international format (e.g., +14155552671).
     */
    private fun getDevicePhoneNumber(): String? {
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            if (telephonyManager == null) {
                Log.w(TAG, "TelephonyManager not available")
                return null
            }

            val rawNumber = telephonyManager.line1Number
            if (!rawNumber.isNullOrBlank()) {
                Log.d(TAG, "Detected phone number from SIM: $rawNumber")

                // Prefer simCountryIso, fall back to networkCountryIso
                val countryIso = telephonyManager.simCountryIso
                    .takeIf { !it.isNullOrBlank() }
                    ?: telephonyManager.networkCountryIso

                return formatPhoneNumberWithCountryCode(rawNumber, countryIso)
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
     * Format a phone number into E.164 international format using the device's SIM country ISO.
     * Uses libphonenumber for accurate country-code resolution across all regions.
     */
    private fun formatPhoneNumberWithCountryCode(number: String, countryIso: String?): String {
        val trimmed = number.trim()

        // Already in international format — validate and normalise via libphonenumber
        if (trimmed.startsWith("+")) {
            return try {
                val phoneUtil = PhoneNumberUtil.getInstance()
                val parsed = phoneUtil.parse(trimmed, null)
                if (phoneUtil.isValidNumber(parsed)) {
                    phoneUtil.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164)
                } else {
                    Log.w(TAG, "Number already has + but failed validation: $trimmed")
                    trimmed
                }
            } catch (e: NumberParseException) {
                Log.w(TAG, "Could not parse number with +: $trimmed", e)
                trimmed
            }
        }

        // Use libphonenumber to resolve the correct country calling code from the ISO
        val regionCode = countryIso?.uppercase()
        if (!regionCode.isNullOrBlank()) {
            try {
                val phoneUtil = PhoneNumberUtil.getInstance()
                val parsed = phoneUtil.parse(trimmed, regionCode)
                if (phoneUtil.isValidNumber(parsed)) {
                    val formatted = phoneUtil.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164)
                    Log.d(TAG, "Formatted $trimmed ($regionCode) → $formatted")
                    return formatted
                } else {
                    Log.w(TAG, "Number not valid for region $regionCode: $trimmed")
                }
            } catch (e: NumberParseException) {
                Log.w(TAG, "libphonenumber could not parse '$trimmed' for region $regionCode", e)
            }
        } else {
            Log.w(TAG, "No country ISO available — cannot determine country code for: $trimmed")
        }

        // Return as-is if we couldn't determine the country code
        return trimmed
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