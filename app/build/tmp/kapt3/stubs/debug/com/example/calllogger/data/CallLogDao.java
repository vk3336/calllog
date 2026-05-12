package com.example.calllogger.data;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0016\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0019\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000bH\'J#\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J\u001b\u0010\u0013\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0014\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u0011\u0010\u0016\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u0011\u0010\u001a\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u0019\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001dJ!\u0010\u001e\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fJ!\u0010 \u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"J\u0011\u0010#\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u0011\u0010$\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u0019\u0010%\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001d\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006&"}, d2 = {"Lcom/example/calllogger/data/CallLogDao;", "", "claimForSync", "", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteOldCallLogs", "", "cutoffTime", "getAllCallLogs", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/calllogger/data/CallLogEntity;", "getCallLogByNumberAndTime", "phoneNumber", "", "timestamp", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCallLogBySystemId", "systemCallId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCallLogCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getFailedCallLogsForRetry", "getPendingCallLogs", "getSyncedCallLogCount", "insertCallLog", "callLog", "(Lcom/example/calllogger/data/CallLogEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markAsFailed", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markAsSynced", "espoId", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "resetFailedToPending", "resetStuckInProgressRows", "updateCallLog", "app_debug"})
@androidx.room.Dao()
public abstract interface CallLogDao {
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertCallLog(@org.jetbrains.annotations.NotNull()
    com.example.calllogger.data.CallLogEntity callLog, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateCallLog(@org.jetbrains.annotations.NotNull()
    com.example.calllogger.data.CallLogEntity callLog, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM call_logs ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.calllogger.data.CallLogEntity>> getAllCallLogs();
    
    @androidx.room.Query(value = "SELECT * FROM call_logs WHERE syncStatus = 0 ORDER BY timestamp ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPendingCallLogs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.calllogger.data.CallLogEntity>> $completion);
    
    /**
     * Get failed records that should be retried (max 3 attempts to avoid infinite loops)
     */
    @androidx.room.Query(value = "SELECT * FROM call_logs WHERE syncStatus = 3 AND syncAttempts < 3 ORDER BY timestamp ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getFailedCallLogsForRetry(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.calllogger.data.CallLogEntity>> $completion);
    
    /**
     * Reset failed records back to PENDING so they are picked up in next sync
     */
    @androidx.room.Query(value = "UPDATE call_logs SET syncStatus = 0 WHERE syncStatus = 3 AND syncAttempts < 3")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object resetFailedToPending(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM call_logs WHERE phoneNumber = :phoneNumber AND timestamp = :timestamp LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCallLogByNumberAndTime(@org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.calllogger.data.CallLogEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM call_logs WHERE systemCallId = :systemCallId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCallLogBySystemId(@org.jetbrains.annotations.NotNull()
    java.lang.String systemCallId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.calllogger.data.CallLogEntity> $completion);
    
    /**
     * Atomic claim: sets IN_PROGRESS only if still PENDING. Returns rows updated (0 or 1).
     */
    @androidx.room.Query(value = "UPDATE call_logs SET syncStatus = 1 WHERE id = :id AND syncStatus = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object claimForSync(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "UPDATE call_logs SET syncStatus = 2, espoId = :espoId WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAsSynced(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String espoId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE call_logs SET syncStatus = 3, syncAttempts = syncAttempts + 1, lastSyncAttempt = :timestamp WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markAsFailed(long id, long timestamp, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * On startup: reset any stuck IN_PROGRESS rows back to PENDING so they are retried.
     */
    @androidx.room.Query(value = "UPDATE call_logs SET syncStatus = 0 WHERE syncStatus = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object resetStuckInProgressRows(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM call_logs")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCallLogCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM call_logs WHERE syncStatus = 2")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSyncedCallLogCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "DELETE FROM call_logs WHERE timestamp < :cutoffTime")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteOldCallLogs(long cutoffTime, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}