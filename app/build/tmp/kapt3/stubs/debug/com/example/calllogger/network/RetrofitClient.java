package com.example.calllogger.network;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u0005R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/example/calllogger/network/RetrofitClient;", "", "()V", "serviceCache", "", "", "Lcom/example/calllogger/network/EspoApiService;", "sharedHttpClient", "Lokhttp3/OkHttpClient;", "clearCache", "", "createEspoApiService", "baseUrl", "apiKey", "Companion", "app_debug"})
public final class RetrofitClient {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.calllogger.network.RetrofitClient INSTANCE;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, com.example.calllogger.network.EspoApiService> serviceCache = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient sharedHttpClient = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.calllogger.network.RetrofitClient.Companion Companion = null;
    
    private RetrofitClient() {
        super();
    }
    
    /**
     * Returns a cached EspoApiService for the given URL + API key.
     * If the same URL and key are used again, the existing instance (and its
     * underlying HTTP connection pool) is reused — no new TCP/TLS handshake needed.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.example.calllogger.network.EspoApiService createEspoApiService(@org.jetbrains.annotations.NotNull()
    java.lang.String baseUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String apiKey) {
        return null;
    }
    
    /**
     * Call this if the URL or API key changes so the old cached service is discarded.
     */
    public final void clearCache() {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/example/calllogger/network/RetrofitClient$Companion;", "", "()V", "INSTANCE", "Lcom/example/calllogger/network/RetrofitClient;", "getInstance", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.calllogger.network.RetrofitClient getInstance() {
            return null;
        }
    }
}