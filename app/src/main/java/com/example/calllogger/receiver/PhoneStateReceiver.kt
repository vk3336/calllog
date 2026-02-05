package com.example.calllogger.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.example.calllogger.service.CallLogService
import com.example.calllogger.util.PermissionUtil

class PhoneStateReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "PhoneStateReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        try {
            Log.d(TAG, "Received broadcast: ${intent.action}")
            
            if (!PermissionUtil.hasAllPermissions(context)) {
                Log.w(TAG, "Missing permissions, skipping")
                return
            }
            
            when (intent.action) {
                TelephonyManager.ACTION_PHONE_STATE_CHANGED -> {
                    val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                    val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                    
                    Log.d(TAG, "Phone state changed: $state, Number: $phoneNumber")
                    
                    when (state) {
                        TelephonyManager.EXTRA_STATE_RINGING -> {
                            Log.d(TAG, "Phone is ringing: $phoneNumber")
                            startCallLogService(context)
                        }
                        TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                            Log.d(TAG, "Call answered or outgoing call started")
                            startCallLogService(context)
                        }
                        TelephonyManager.EXTRA_STATE_IDLE -> {
                            Log.d(TAG, "Call ended - triggering immediate sync")
                            // Delay to ensure call log is written by system
                            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                startCallLogService(context)
                            }, 2000) // 2 second delay
                        }
                    }
                }
                Intent.ACTION_NEW_OUTGOING_CALL -> {
                    val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
                    Log.d(TAG, "Outgoing call detected: $phoneNumber")
                    startCallLogService(context)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in PhoneStateReceiver", e)
        }
    }
    
    private fun startCallLogService(context: Context) {
        try {
            val serviceIntent = Intent(context, CallLogService::class.java)
            context.startForegroundService(serviceIntent)
            Log.d(TAG, "CallLogService started successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start CallLogService", e)
        }
    }
}