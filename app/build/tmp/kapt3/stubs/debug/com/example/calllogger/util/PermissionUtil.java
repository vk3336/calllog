package com.example.calllogger.util;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0011\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018R\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006\u00a8\u0006\u0019"}, d2 = {"Lcom/example/calllogger/util/PermissionUtil;", "", "()V", "NOTIFICATION_PERMISSIONS", "", "", "[Ljava/lang/String;", "PERMISSION_REQUEST_CODE", "", "PHONE_NUMBER_PERMISSIONS", "REQUIRED_PERMISSIONS", "getAllRequiredPermissions", "()[Ljava/lang/String;", "getMissingPermissions", "", "context", "Landroid/content/Context;", "hasAllPermissions", "", "hasCallLogPermission", "hasPhoneStatePermission", "requestAllPermissions", "", "activity", "Landroid/app/Activity;", "app_debug"})
public final class PermissionUtil {
    public static final int PERMISSION_REQUEST_CODE = 1001;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] REQUIRED_PERMISSIONS = {"android.permission.READ_CALL_LOG", "android.permission.READ_PHONE_STATE", "android.permission.READ_CONTACTS"};
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] PHONE_NUMBER_PERMISSIONS = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] NOTIFICATION_PERMISSIONS = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.calllogger.util.PermissionUtil INSTANCE = null;
    
    private PermissionUtil() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String[] getAllRequiredPermissions() {
        return null;
    }
    
    public final boolean hasAllPermissions(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    public final boolean hasCallLogPermission(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    public final boolean hasPhoneStatePermission(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
    
    public final void requestAllPermissions(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<java.lang.String> getMissingPermissions(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
}