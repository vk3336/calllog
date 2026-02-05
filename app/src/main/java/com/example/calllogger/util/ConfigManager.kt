package com.example.calllogger.util

import android.content.Context
import android.content.SharedPreferences

class ConfigManager private constructor(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("call_logger_config", Context.MODE_PRIVATE)
    
    companion object {
        @Volatile
        private var INSTANCE: ConfigManager? = null
        
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
        get() = sharedPreferences.getString(KEY_PHONE_NUMBER, null)
        set(value) = sharedPreferences.edit().putString(KEY_PHONE_NUMBER, value).apply()
    
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
        return !phoneNumber.isNullOrBlank()
    }
    
    fun isEspoConfigured(): Boolean {
        return !phoneNumber.isNullOrBlank() && 
               !espoBaseUrl.isNullOrBlank() && 
               !espoApiKey.isNullOrBlank()
    }
    
    fun clearConfig() {
        sharedPreferences.edit().clear().apply()
    }
}