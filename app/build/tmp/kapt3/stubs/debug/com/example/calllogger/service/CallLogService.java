package com.example.calllogger.service;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\u0018\u0000 (2\u00020\u0001:\u0001(B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\b\u0010\u0019\u001a\u00020\u000eH\u0002J\u0014\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\b\u0010\u001e\u001a\u00020\u000eH\u0016J\b\u0010\u001f\u001a\u00020\u000eH\u0016J\"\u0010 \u001a\u00020!2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\"\u001a\u00020!2\u0006\u0010#\u001a\u00020!H\u0016J\u0011\u0010$\u001a\u00020\u000eH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%J\b\u0010&\u001a\u00020\u000eH\u0002J\u0011\u0010\'\u001a\u00020\u000eH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010%R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006)"}, d2 = {"Lcom/example/calllogger/service/CallLogService;", "Landroid/app/Service;", "()V", "actualEspoUrl", "", "configManager", "Lcom/example/calllogger/util/ConfigManager;", "database", "Lcom/example/calllogger/data/AppDatabase;", "espoApiService", "Lcom/example/calllogger/network/EspoApiService;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "broadcastApiResponse", "", "status", "message", "createEspoCallRequest", "Lcom/example/calllogger/network/EspoCallRequest;", "call", "Lcom/example/calllogger/data/CallLogEntity;", "createNotification", "Landroid/app/Notification;", "getContactName", "phoneNumber", "initializeEspoApi", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "", "flags", "startId", "readAndStoreCallLogs", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startCallLogMonitoring", "syncUnsyncedCallLogs", "Companion", "app_debug"})
public final class CallLogService extends android.app.Service {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    private com.example.calllogger.data.AppDatabase database;
    private com.example.calllogger.util.ConfigManager configManager;
    @org.jetbrains.annotations.Nullable()
    private com.example.calllogger.network.EspoApiService espoApiService;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String actualEspoUrl;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "CallLogService";
    private static final int NOTIFICATION_ID = 1001;
    private static final long SYNC_INTERVAL = 30000L;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.calllogger.service.CallLogService.Companion Companion = null;
    
    public CallLogService() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @java.lang.Override()
    public int onStartCommand(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable()
    android.content.Intent intent) {
        return null;
    }
    
    @java.lang.Override()
    public void onDestroy() {
    }
    
    private final android.app.Notification createNotification() {
        return null;
    }
    
    private final void initializeEspoApi() {
    }
    
    private final void startCallLogMonitoring() {
    }
    
    private final java.lang.Object readAndStoreCallLogs(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.String getContactName(java.lang.String phoneNumber) {
        return null;
    }
    
    private final java.lang.Object syncUnsyncedCallLogs(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void broadcastApiResponse(java.lang.String status, java.lang.String message) {
    }
    
    private final com.example.calllogger.network.EspoCallRequest createEspoCallRequest(com.example.calllogger.data.CallLogEntity call) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/example/calllogger/service/CallLogService$Companion;", "", "()V", "NOTIFICATION_ID", "", "SYNC_INTERVAL", "", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}