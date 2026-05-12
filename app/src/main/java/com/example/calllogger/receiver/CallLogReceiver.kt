package com.example.calllogger.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.example.calllogger.service.CallLogService
import com.example.calllogger.util.PermissionUtil

class CallLogReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "CallLogReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        try {
            Log.d(TAG, "Received broadcast: ${intent.action}")
            
            if (!PermissionUtil.hasAllPermissions(context)) {
                Log.w(TAG, "Missing permissions, skipping")
                return
            }
            
            when (intent.action) {
                Intent.ACTION_NEW_OUTGOING_CALL -> {
                    val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
                    Log.d(TAG, "Outgoing call detected: $phoneNumber")
                    
                    // Start service to capture the outgoing call
                    startCallLogService(context)
                }
                TelephonyManager.ACTION_PHONE_STATE_CHANGED -> {
                    val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                    Log.d(TAG, "Phone state changed: $state")
                    
                    if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                        // Call ended, wait a bit then capture the call log
                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                            startCallLogService(context)
                        }, 3000) // 3 second delay to ensure call log is written
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in CallLogReceiver", e)
        }
    }
    
    private fun startCallLogService(context: Context) {
        try {
            val serviceIntent = Intent(context, CallLogService::class.java)
            context.startForegroundService(serviceIntent)
            Log.d(TAG, "CallLogService started from CallLogReceiver")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start CallLogService", e)
        }
    }
}