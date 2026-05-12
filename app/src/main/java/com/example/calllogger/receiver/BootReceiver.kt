package com.example.calllogger.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.calllogger.service.CallLogService
import com.example.calllogger.util.ConfigManager

/**
 * Starts the CallLogService (and reschedules WorkManager) automatically
 * after the phone boots or restarts.
 */
class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        Log.d(TAG, "Boot completed — restarting CallLogService")

        val configManager = ConfigManager.getInstance(context)

        // Only restart if sync was enabled before reboot
        if (configManager.isSyncEnabled && configManager.isEspoConfigured()) {
            try {
                val serviceIntent = Intent(context, CallLogService::class.java)
                context.startForegroundService(serviceIntent)
                Log.d(TAG, "✅ CallLogService restarted after boot")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Failed to restart service after boot", e)
            }
        } else {
            Log.d(TAG, "Sync not enabled or ESPO not configured — skipping auto-start")
        }
    }
}
