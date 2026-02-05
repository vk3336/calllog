package com.example.calllogger.util;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u0000 )2\u00020\u0001:\u0001)B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010%\u001a\u00020&J\u0006\u0010\'\u001a\u00020\u000fJ\u0006\u0010(\u001a\u00020\u000fR(\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR(\u0010\f\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\r\u0010\t\"\u0004\b\u000e\u0010\u000bR$\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0005\u001a\u00020\u000f8F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R$\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u00148F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R(\u0010\u001a\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b\u001b\u0010\t\"\u0004\b\u001c\u0010\u000bR\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R(\u0010\u001f\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b \u0010\t\"\u0004\b!\u0010\u000bR(\u0010\"\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u00068F@FX\u0086\u000e\u00a2\u0006\f\u001a\u0004\b#\u0010\t\"\u0004\b$\u0010\u000b\u00a8\u0006*"}, d2 = {"Lcom/example/calllogger/util/ConfigManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "value", "", "espoApiKey", "getEspoApiKey", "()Ljava/lang/String;", "setEspoApiKey", "(Ljava/lang/String;)V", "espoBaseUrl", "getEspoBaseUrl", "setEspoBaseUrl", "", "isSyncEnabled", "()Z", "setSyncEnabled", "(Z)V", "", "lastSyncTime", "getLastSyncTime", "()J", "setLastSyncTime", "(J)V", "phoneNumber", "getPhoneNumber", "setPhoneNumber", "sharedPreferences", "Landroid/content/SharedPreferences;", "sim1Name", "getSim1Name", "setSim1Name", "sim2Name", "getSim2Name", "setSim2Name", "clearConfig", "", "isConfigured", "isEspoConfigured", "Companion", "app_debug"})
public final class ConfigManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences sharedPreferences = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.calllogger.util.ConfigManager INSTANCE;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_PHONE_NUMBER = "phone_number";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ESPO_BASE_URL = "espo_base_url";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ESPO_API_KEY = "espo_api_key";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SYNC_ENABLED = "sync_enabled";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_LAST_SYNC_TIME = "last_sync_time";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SIM1_NAME = "sim1_name";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_SIM2_NAME = "sim2_name";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.calllogger.util.ConfigManager.Companion Companion = null;
    
    private ConfigManager(android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPhoneNumber() {
        return null;
    }
    
    public final void setPhoneNumber(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEspoBaseUrl() {
        return null;
    }
    
    public final void setEspoBaseUrl(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEspoApiKey() {
        return null;
    }
    
    public final void setEspoApiKey(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
    }
    
    public final boolean isSyncEnabled() {
        return false;
    }
    
    public final void setSyncEnabled(boolean value) {
    }
    
    public final long getLastSyncTime() {
        return 0L;
    }
    
    public final void setLastSyncTime(long value) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSim1Name() {
        return null;
    }
    
    public final void setSim1Name(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSim2Name() {
        return null;
    }
    
    public final void setSim2Name(@org.jetbrains.annotations.Nullable()
    java.lang.String value) {
    }
    
    public final boolean isConfigured() {
        return false;
    }
    
    public final boolean isEspoConfigured() {
        return false;
    }
    
    public final void clearConfig() {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u000fR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/example/calllogger/util/ConfigManager$Companion;", "", "()V", "INSTANCE", "Lcom/example/calllogger/util/ConfigManager;", "KEY_ESPO_API_KEY", "", "KEY_ESPO_BASE_URL", "KEY_LAST_SYNC_TIME", "KEY_PHONE_NUMBER", "KEY_SIM1_NAME", "KEY_SIM2_NAME", "KEY_SYNC_ENABLED", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.calllogger.util.ConfigManager getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}