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
    
    @Query("SELECT * FROM call_logs WHERE isSynced = 0 ORDER BY timestamp ASC")
    suspend fun getUnsyncedCallLogs(): List<CallLogEntity>
    
    @Query("SELECT * FROM call_logs WHERE phoneNumber = :phoneNumber AND timestamp = :timestamp LIMIT 1")
    suspend fun getCallLogByNumberAndTime(phoneNumber: String, timestamp: Long): CallLogEntity?
    
    @Query("UPDATE call_logs SET isSynced = 1, espoId = :espoId WHERE id = :id")
    suspend fun markAsSynced(id: Long, espoId: String)
    
    @Query("UPDATE call_logs SET syncAttempts = syncAttempts + 1, lastSyncAttempt = :timestamp WHERE id = :id")
    suspend fun incrementSyncAttempts(id: Long, timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM call_logs")
    suspend fun getCallLogCount(): Int
    
    @Query("SELECT COUNT(*) FROM call_logs WHERE isSynced = 1")
    suspend fun getSyncedCallLogCount(): Int
    
    @Query("DELETE FROM call_logs WHERE timestamp < :cutoffTime")
    suspend fun deleteOldCallLogs(cutoffTime: Long)
}