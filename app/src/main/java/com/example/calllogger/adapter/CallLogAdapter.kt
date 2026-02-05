package com.example.calllogger.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calllogger.R
import com.example.calllogger.data.CallLogEntity
import com.example.calllogger.databinding.ItemCallLogBinding
import com.example.calllogger.util.ConfigManager
import java.text.SimpleDateFormat
import java.util.*

class CallLogAdapter : ListAdapter<CallLogEntity, CallLogAdapter.CallLogViewHolder>(CallLogDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val binding = ItemCallLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CallLogViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class CallLogViewHolder(private val binding: ItemCallLogBinding) : RecyclerView.ViewHolder(binding.root) {
        
        private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        
        fun bind(callLog: CallLogEntity) {
            binding.apply {
                // Contact name and phone number with direction info
                textViewContactName.text = callLog.contactName ?: root.context.getString(R.string.unknown_contact)
                
                // Show From/To information based on call direction
                val directionInfo = when (callLog.callType) {
                    CallLogEntity.INCOMING_TYPE -> "From: ${formatPhoneNumber(callLog.phoneNumber)}"
                    CallLogEntity.OUTGOING_TYPE -> "To: ${formatPhoneNumber(callLog.phoneNumber)}"
                    CallLogEntity.MISSED_TYPE -> "Missed from: ${formatPhoneNumber(callLog.phoneNumber)}"
                    else -> formatPhoneNumber(callLog.phoneNumber)
                }
                textViewPhoneNumber.text = directionInfo
                
                // Date and time
                val callDate = Date(callLog.timestamp)
                textViewTime.text = timeFormat.format(callDate)
                textViewDate.text = dateFormat.format(callDate)
                
                // Call type setup
                setupCallType(callLog.callType)
                
                // Duration (remove "Duration:" prefix since it's in the label)
                textViewDuration.text = formatDuration(callLog.duration)
                
                // Sync status
                setupSyncStatus(callLog)
                
                // Call status
                setupCallStatus(callLog)
                
                // === ALL 16 FIELDS DISPLAYED ===
                
                // 1. Location
                textViewLocation.text = callLog.geocodedLocation?.takeIf { it.isNotBlank() } ?: "Unknown"
                
                // 2. Country
                textViewCountry.text = callLog.countryIso?.takeIf { it.isNotBlank() } ?: "Unknown"
                
                // 3. Phone Account (SIM Card)
                textViewPhoneAccount.text = getSimCardName(callLog.phoneAccountId)
                
                // 4. Features
                textViewFeatures.text = callLog.getCallFeaturesString()
                
                // 5. Number Presentation
                textViewPresentation.text = callLog.getNumberPresentationString()
                
                // 6. Read Status
                textViewReadStatus.text = if (callLog.isRead == 1) "Read" else "Unread"
                
                // 7. New Status
                textViewNewStatus.text = if (callLog.isNew == 1) "New" else "Old"
                
                // 8. Via Number (show only if present)
                if (!callLog.viaNumber.isNullOrBlank()) {
                    layoutViaNumber.visibility = View.VISIBLE
                    textViewViaNumber.text = formatPhoneNumber(callLog.viaNumber)
                } else {
                    layoutViaNumber.visibility = View.GONE
                }
                
                // 9. Data Usage (show only if present)
                if (callLog.dataUsage != null && callLog.dataUsage > 0) {
                    layoutDataUsage.visibility = View.VISIBLE
                    textViewDataUsage.text = formatDataUsage(callLog.dataUsage)
                } else {
                    layoutDataUsage.visibility = View.GONE
                }
                
                // 10. Post Dial Digits (show only if present)
                if (!callLog.postDialDigits.isNullOrBlank()) {
                    layoutPostDialDigits.visibility = View.VISIBLE
                    textViewPostDialDigits.text = callLog.postDialDigits
                } else {
                    layoutPostDialDigits.visibility = View.GONE
                }
                
                // 11. Voicemail Transcription (show only if present)
                if (!callLog.voicemailTranscription.isNullOrBlank()) {
                    layoutTranscription.visibility = View.VISIBLE
                    textViewTranscription.text = callLog.voicemailTranscription
                } else {
                    layoutTranscription.visibility = View.GONE
                }
                
                // 12. Sync Attempts
                textViewSyncAttempts.text = callLog.syncAttempts.toString()
                
                // 13. ESPO ID
                textViewEspoId.text = callLog.espoId?.takeIf { it.isNotBlank() } ?: "Not synced"
                
                // Note: Fields 14-16 are automatically displayed:
                // 14. Phone Number (textViewPhoneNumber)
                // 15. Contact Name (textViewContactName) 
                // 16. Timestamp (textViewTime + textViewDate)
            }
        }
        
        private fun setupCallType(callType: Int) {
            binding.apply {
                when (callType) {
                    CallLogEntity.INCOMING_TYPE -> {
                        imageViewCallType.setImageResource(R.drawable.ic_call_incoming)
                        viewCallTypeBackground.background = ContextCompat.getDrawable(root.context, R.drawable.bg_call_incoming)
                        textViewCallTypeLabel.text = root.context.getString(R.string.incoming_call)
                        textViewCallTypeLabel.setTextColor(ContextCompat.getColor(root.context, R.color.incoming_call))
                    }
                    CallLogEntity.OUTGOING_TYPE -> {
                        imageViewCallType.setImageResource(R.drawable.ic_call_outgoing)
                        viewCallTypeBackground.background = ContextCompat.getDrawable(root.context, R.drawable.bg_call_outgoing)
                        textViewCallTypeLabel.text = root.context.getString(R.string.outgoing_call)
                        textViewCallTypeLabel.setTextColor(ContextCompat.getColor(root.context, R.color.outgoing_call))
                    }
                    CallLogEntity.MISSED_TYPE -> {
                        imageViewCallType.setImageResource(R.drawable.ic_call_missed)
                        viewCallTypeBackground.background = ContextCompat.getDrawable(root.context, R.drawable.bg_call_missed)
                        textViewCallTypeLabel.text = root.context.getString(R.string.missed_call)
                        textViewCallTypeLabel.setTextColor(ContextCompat.getColor(root.context, R.color.missed_call))
                    }
                    CallLogEntity.VOICEMAIL_TYPE -> {
                        imageViewCallType.setImageResource(R.drawable.ic_call_missed)
                        viewCallTypeBackground.background = ContextCompat.getDrawable(root.context, R.drawable.bg_call_missed)
                        textViewCallTypeLabel.text = "VOICEMAIL"
                        textViewCallTypeLabel.setTextColor(ContextCompat.getColor(root.context, R.color.missed_call))
                    }
                    CallLogEntity.REJECTED_TYPE -> {
                        imageViewCallType.setImageResource(R.drawable.ic_call_missed)
                        viewCallTypeBackground.background = ContextCompat.getDrawable(root.context, R.drawable.bg_call_missed)
                        textViewCallTypeLabel.text = "REJECTED"
                        textViewCallTypeLabel.setTextColor(ContextCompat.getColor(root.context, R.color.failed_red))
                    }
                    CallLogEntity.BLOCKED_TYPE -> {
                        imageViewCallType.setImageResource(R.drawable.ic_call_missed)
                        viewCallTypeBackground.background = ContextCompat.getDrawable(root.context, R.drawable.bg_call_missed)
                        textViewCallTypeLabel.text = "BLOCKED"
                        textViewCallTypeLabel.setTextColor(ContextCompat.getColor(root.context, R.color.failed_red))
                    }
                    else -> {
                        imageViewCallType.setImageResource(R.drawable.ic_phone)
                        viewCallTypeBackground.background = ContextCompat.getDrawable(root.context, R.drawable.bg_call_incoming)
                        textViewCallTypeLabel.text = root.context.getString(R.string.unknown_call)
                        textViewCallTypeLabel.setTextColor(ContextCompat.getColor(root.context, R.color.text_secondary))
                    }
                }
            }
        }
        
        private fun setupSyncStatus(callLog: CallLogEntity) {
            binding.apply {
                val syncContainer = textViewSyncStatus.parent as ViewGroup
                
                if (callLog.isSynced) {
                    imageViewSyncStatus.setImageResource(R.drawable.ic_sync_success)
                    textViewSyncStatus.text = root.context.getString(R.string.synced)
                    syncContainer.background = ContextCompat.getDrawable(root.context, R.drawable.bg_sync_status)
                    syncContainer.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.synced_green)
                } else {
                    if (callLog.syncAttempts > 0) {
                        imageViewSyncStatus.setImageResource(R.drawable.ic_sync_failed)
                        textViewSyncStatus.text = "${root.context.getString(R.string.failed)} (${callLog.syncAttempts})"
                        syncContainer.background = ContextCompat.getDrawable(root.context, R.drawable.bg_sync_status)
                        syncContainer.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.failed_red)
                    } else {
                        imageViewSyncStatus.setImageResource(R.drawable.ic_sync_pending)
                        textViewSyncStatus.text = root.context.getString(R.string.pending)
                        syncContainer.background = ContextCompat.getDrawable(root.context, R.drawable.bg_sync_status)
                        syncContainer.backgroundTintList = ContextCompat.getColorStateList(root.context, R.color.pending_orange)
                    }
                }
            }
        }
        
        private fun setupCallStatus(callLog: CallLogEntity) {
            binding.apply {
                val statusContainer = layoutCallStatus
                val statusText = textViewCallStatus
                
                val callStatus = callLog.getCallStatusString()
                statusText.text = callStatus
                
                when (callLog.callType) {
                    CallLogEntity.MISSED_TYPE -> {
                        statusContainer.background = ContextCompat.getDrawable(root.context, R.drawable.bg_status_missed)
                    }
                    CallLogEntity.REJECTED_TYPE, CallLogEntity.BLOCKED_TYPE -> {
                        statusContainer.background = ContextCompat.getDrawable(root.context, R.drawable.bg_status_missed)
                    }
                    else -> {
                        if (callLog.duration > 0) {
                            statusContainer.background = ContextCompat.getDrawable(root.context, R.drawable.bg_status_completed)
                        } else {
                            statusContainer.background = ContextCompat.getDrawable(root.context, R.drawable.bg_status_no_answer)
                        }
                    }
                }
            }
        }
        
        private fun formatPhoneNumber(phoneNumber: String): String {
            // Simple phone number formatting
            return when {
                phoneNumber.length == 10 -> {
                    "${phoneNumber.substring(0, 3)}-${phoneNumber.substring(3, 6)}-${phoneNumber.substring(6)}"
                }
                phoneNumber.length == 11 && phoneNumber.startsWith("1") -> {
                    "+1 (${phoneNumber.substring(1, 4)}) ${phoneNumber.substring(4, 7)}-${phoneNumber.substring(7)}"
                }
                phoneNumber.startsWith("+") -> phoneNumber
                else -> phoneNumber
            }
        }
        
        private fun formatDuration(seconds: Long): String {
            return if (seconds == 0L) {
                "0 seconds"
            } else {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val remainingSeconds = seconds % 60
                
                when {
                    hours > 0 -> "${hours}h ${minutes}m ${remainingSeconds}s"
                    minutes > 0 -> "${minutes}m ${remainingSeconds}s"
                    else -> "${remainingSeconds}s"
                }
            }
        }
        
        private fun formatDataUsage(bytes: Long): String {
            return when {
                bytes < 1024 -> "${bytes}B"
                bytes < 1024 * 1024 -> "${bytes / 1024}KB"
                bytes < 1024 * 1024 * 1024 -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
                else -> String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
            }
        }
        
        private fun getSimCardName(phoneAccountId: String?): String {
            val configManager = ConfigManager.getInstance(binding.root.context)
            return when {
                phoneAccountId == null -> "Default"
                phoneAccountId.contains("1") || phoneAccountId.contains("sim1", ignoreCase = true) -> 
                    configManager.sim1Name ?: "Personal SIM"
                phoneAccountId.contains("2") || phoneAccountId.contains("sim2", ignoreCase = true) -> 
                    configManager.sim2Name ?: "Work SIM"
                phoneAccountId.contains("esim", ignoreCase = true) -> "eSIM"
                phoneAccountId.isNotBlank() -> "SIM Card"
                else -> "Default"
            }
        }
    }
    
    class CallLogDiffCallback : DiffUtil.ItemCallback<CallLogEntity>() {
        override fun areItemsTheSame(oldItem: CallLogEntity, newItem: CallLogEntity): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: CallLogEntity, newItem: CallLogEntity): Boolean {
            return oldItem == newItem
        }
    }
}