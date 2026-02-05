package com.example.calllogger.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_logs")
data class CallLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // Core call information
    val phoneNumber: String,
    val contactName: String?,
    val callType: Int, // 1=incoming, 2=outgoing, 3=missed, 4=voicemail, 5=rejected, 6=blocked
    val duration: Long, // in seconds
    val timestamp: Long, // call time in milliseconds
    
    // Additional Android call log fields
    val geocodedLocation: String?, // Geographic location where call was made
    val phoneAccountId: String?, // Which SIM card/account was used
    val viaNumber: String?, // Number the call came through (for forwarded calls)
    val voicemailTranscription: String?, // Text transcription of voicemail
    val isRead: Int = 0, // Whether call log entry was read (0=unread, 1=read)
    val isNew: Int = 1, // Whether this is a new call log entry (0=old, 1=new)
    val countryIso: String?, // Country code (e.g., "US", "IN")
    val dataUsage: Long?, // Data used during call (for VoIP calls)
    val features: Int = 0, // Call features bitmask (video call, HD audio, etc.)
    val numberPresentation: Int = 1, // How number should be presented (allowed, restricted, etc.)
    val postDialDigits: String?, // Digits dialed after connection
    
    // Database management fields
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    
    // Sync fields (for ESPO integration)
    val isSynced: Boolean = false,
    val syncAttempts: Int = 0,
    val lastSyncAttempt: Long = 0,
    val espoId: String? = null,
    
    // Call completion status (separate from sync status)
    val isCallCompleted: Boolean = true // Most calls are completed when logged
) {
    companion object {
        // Call types
        const val INCOMING_TYPE = 1
        const val OUTGOING_TYPE = 2
        const val MISSED_TYPE = 3
        const val VOICEMAIL_TYPE = 4
        const val REJECTED_TYPE = 5
        const val BLOCKED_TYPE = 6
        
        // Number presentation types
        const val PRESENTATION_ALLOWED = 1
        const val PRESENTATION_RESTRICTED = 2
        const val PRESENTATION_UNKNOWN = 3
        const val PRESENTATION_PAYPHONE = 4
        
        // Call features bitmask
        const val FEATURES_VIDEO = 1
        const val FEATURES_WIFI = 2
        const val FEATURES_HD_CALL = 4
        const val FEATURES_VOLTE = 8
        const val FEATURES_VT_IS_BIDIRECTIONAL = 16
        const val FEATURES_RTT = 32
    }
    
    fun getCallTypeString(): String {
        return when (callType) {
            INCOMING_TYPE -> "Incoming"
            OUTGOING_TYPE -> "Outgoing"
            MISSED_TYPE -> "Missed"
            VOICEMAIL_TYPE -> "Voicemail"
            REJECTED_TYPE -> "Rejected"
            BLOCKED_TYPE -> "Blocked"
            else -> "Unknown"
        }
    }
    
    fun getNumberPresentationString(): String {
        return when (numberPresentation) {
            PRESENTATION_ALLOWED -> "Allowed"
            PRESENTATION_RESTRICTED -> "Restricted"
            PRESENTATION_UNKNOWN -> "Unknown"
            PRESENTATION_PAYPHONE -> "Payphone"
            else -> "Unknown"
        }
    }
    
    fun getCallFeaturesString(): String {
        val features = mutableListOf<String>()
        if (this.features and FEATURES_VIDEO != 0) features.add("Video")
        if (this.features and FEATURES_WIFI != 0) features.add("WiFi")
        if (this.features and FEATURES_HD_CALL != 0) features.add("HD")
        if (this.features and FEATURES_VOLTE != 0) features.add("VoLTE")
        if (this.features and FEATURES_VT_IS_BIDIRECTIONAL != 0) features.add("Bidirectional Video")
        if (this.features and FEATURES_RTT != 0) features.add("RTT")
        return if (features.isEmpty()) "None" else features.joinToString(", ")
    }
    
    fun getCallStatusString(): String {
        return when {
            callType == MISSED_TYPE -> "Missed"
            callType == REJECTED_TYPE -> "Rejected"
            callType == BLOCKED_TYPE -> "Blocked"
            duration > 0 -> "Completed"
            else -> "No Answer"
        }
    }
}