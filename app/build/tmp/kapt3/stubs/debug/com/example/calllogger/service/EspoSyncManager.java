package com.example.calllogger.service;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0019\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u0016H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u0019\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u0016H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001a"}, d2 = {"Lcom/example/calllogger/service/EspoSyncManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "configManager", "Lcom/example/calllogger/util/ConfigManager;", "database", "Lcom/example/calllogger/data/AppDatabase;", "mutex", "Lkotlinx/coroutines/sync/Mutex;", "broadcast", "", "status", "", "message", "buildRequest", "Lcom/example/calllogger/network/EspoCallRequest;", "call", "Lcom/example/calllogger/data/CallLogEntity;", "doSync", "espoApi", "Lcom/example/calllogger/network/EspoApiService;", "(Lcom/example/calllogger/network/EspoApiService;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncPendingCalls", "Companion", "app_debug"})
public final class EspoSyncManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.sync.Mutex mutex = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.calllogger.data.AppDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.calllogger.util.ConfigManager configManager = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "EspoSyncManager";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.calllogger.service.EspoSyncManager.Companion Companion = null;
    
    public EspoSyncManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Sync all pending calls. Uses a Mutex so only one sync runs at a time.
     * If already running, the new call is silently dropped.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object syncPendingCalls(@org.jetbrains.annotations.NotNull()
    com.example.calllogger.network.EspoApiService espoApi, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object doSync(com.example.calllogger.network.EspoApiService espoApi, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.example.calllogger.network.EspoCallRequest buildRequest(com.example.calllogger.data.CallLogEntity call) {
        return null;
    }
    
    private final void broadcast(java.lang.String status, java.lang.String message) {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/example/calllogger/service/EspoSyncManager$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}