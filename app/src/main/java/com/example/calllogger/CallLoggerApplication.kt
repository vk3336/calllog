package com.example.calllogger

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class CallLoggerApplication : Application() {
    
    companion object {
        const val CHANNEL_ID = "call_log_channel"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Call Log Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notification for call log monitoring service"
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}