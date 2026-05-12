package com.example.calllogger.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CallLogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCallLog(callLog: CallLogEntity): Long

    @Update
    suspend fun updateCallLog(callLog: CallLogEntity)

    @Query("SELECT * FROM call_logs ORDER BY timestamp DESC")
    fun getAllCallLogs(): Flow<List<CallLogEntity>>

    @Query("SELECT * FROM call_logs WHERE syncStatus = ${CallLogEntity.SYNC_PENDING} ORDER BY timestamp ASC")
    suspend fun getPendingCallLogs(): List<CallLogEntity>

    /** Get failed records that should be retried (max 3 attempts to avoid infinite loops) */
    @Query("SELECT * FROM call_logs WHERE syncStatus = ${CallLogEntity.SYNC_FAILED} AND syncAttempts < 3 ORDER BY timestamp ASC")
    suspend fun getFailedCallLogsForRetry(): List<CallLogEntity>

    /** Reset failed records back to PENDING so they are picked up in next sync */
    @Query("UPDATE call_logs SET syncStatus = ${CallLogEntity.SYNC_PENDING} WHERE syncStatus = ${CallLogEntity.SYNC_FAILED} AND syncAttempts < 3")
    suspend fun resetFailedToPending(): Int

    @Query("SELECT * FROM call_logs WHERE phoneNumber = :phoneNumber AND timestamp = :timestamp LIMIT 1")
    suspend fun getCallLogByNumberAndTime(phoneNumber: String, timestamp: Long): CallLogEntity?

    @Query("SELECT * FROM call_logs WHERE systemCallId = :systemCallId LIMIT 1")
    suspend fun getCallLogBySystemId(systemCallId: String): CallLogEntity?

    /** Atomic claim: sets IN_PROGRESS only if still PENDING. Returns rows updated (0 or 1). */
    @Query("UPDATE call_logs SET syncStatus = ${CallLogEntity.SYNC_IN_PROGRESS} WHERE id = :id AND syncStatus = ${CallLogEntity.SYNC_PENDING}")
    suspend fun claimForSync(id: Long): Int

    @Query("UPDATE call_logs SET syncStatus = ${CallLogEntity.SYNC_SYNCED}, espoId = :espoId WHERE id = :id")
    suspend fun markAsSynced(id: Long, espoId: String)

    @Query("UPDATE call_logs SET syncStatus = ${CallLogEntity.SYNC_FAILED}, syncAttempts = syncAttempts + 1, lastSyncAttempt = :timestamp WHERE id = :id")
    suspend fun markAsFailed(id: Long, timestamp: Long)

    /** On startup: reset any stuck IN_PROGRESS rows back to PENDING so they are retried. */
    @Query("UPDATE call_logs SET syncStatus = ${CallLogEntity.SYNC_PENDING} WHERE syncStatus = ${CallLogEntity.SYNC_IN_PROGRESS}")
    suspend fun resetStuckInProgressRows()

    @Query("SELECT COUNT(*) FROM call_logs")
    suspend fun getCallLogCount(): Int

    @Query("SELECT COUNT(*) FROM call_logs WHERE syncStatus = ${CallLogEntity.SYNC_SYNCED}")
    suspend fun getSyncedCallLogCount(): Int

    @Query("DELETE FROM call_logs WHERE timestamp < :cutoffTime")
    suspend fun deleteOldCallLogs(cutoffTime: Long)
}
