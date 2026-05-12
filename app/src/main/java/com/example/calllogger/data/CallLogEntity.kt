package com.example.calllogger.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "call_logs",
    indices = [Index(value = ["systemCallId"], unique = true)]
)
data class CallLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Android system call log ID — used to prevent duplicate DB rows
    val systemCallId: String? = null,

    // Core call information
    val phoneNumber: String,
    val contactName: String?,
    val callType: Int,
    val duration: Long,
    val timestamp: Long,

    // Additional Android call log fields
    val geocodedLocation: String?,
    val phoneAccountId: String?,
    val viaNumber: String?,
    val voicemailTranscription: String?,
    val isRead: Int = 0,
    val isNew: Int = 1,
    val countryIso: String?,
    val dataUsage: Long?,
    val features: Int = 0,
    val numberPresentation: Int = 1,
    val postDialDigits: String?,

    // Database management fields
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),

    // Sync status: 0=PENDING, 1=IN_PROGRESS, 2=SYNCED, 3=FAILED
    val syncStatus: Int = SYNC_PENDING,
    val syncAttempts: Int = 0,
    val lastSyncAttempt: Long = 0,
    val espoId: String? = null,

    val isCallCompleted: Boolean = true
) {
    companion object {
        // Call types
        const val INCOMING_TYPE = 1
        const val OUTGOING_TYPE = 2
        const val MISSED_TYPE = 3
        const val VOICEMAIL_TYPE = 4
        const val REJECTED_TYPE = 5
        const val BLOCKED_TYPE = 6
        const val ANSWERED_EXTERNALLY_TYPE = 7

        // Sync status values
        const val SYNC_PENDING = 0
        const val SYNC_IN_PROGRESS = 1
        const val SYNC_SYNCED = 2
        const val SYNC_FAILED = 3

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

    fun getCallTypeString(): String = when (callType) {
        INCOMING_TYPE -> "Incoming"
        OUTGOING_TYPE -> "Outgoing"
        MISSED_TYPE -> "Missed"
        VOICEMAIL_TYPE -> "Voicemail"
        REJECTED_TYPE -> "Rejected"
        BLOCKED_TYPE -> "Blocked"
        else -> "Unknown"
    }

    fun getNumberPresentationString(): String = when (numberPresentation) {
        PRESENTATION_ALLOWED -> "Allowed"
        PRESENTATION_RESTRICTED -> "Restricted"
        PRESENTATION_UNKNOWN -> "Unknown"
        PRESENTATION_PAYPHONE -> "Payphone"
        else -> "Unknown"
    }

    fun getCallFeaturesString(): String {
        val list = mutableListOf<String>()
        if (features and FEATURES_VIDEO != 0) list.add("Video")
        if (features and FEATURES_WIFI != 0) list.add("WiFi")
        if (features and FEATURES_HD_CALL != 0) list.add("HD")
        if (features and FEATURES_VOLTE != 0) list.add("VoLTE")
        if (features and FEATURES_VT_IS_BIDIRECTIONAL != 0) list.add("Bidirectional Video")
        if (features and FEATURES_RTT != 0) list.add("RTT")
        return if (list.isEmpty()) "None" else list.joinToString(", ")
    }

    fun getCallStatusString(): String = when {
        callType == MISSED_TYPE -> "Missed"
        callType == REJECTED_TYPE -> "Rejected"
        callType == BLOCKED_TYPE -> "Blocked"
        duration > 0 -> "Completed"
        else -> "No Answer"
    }
}
