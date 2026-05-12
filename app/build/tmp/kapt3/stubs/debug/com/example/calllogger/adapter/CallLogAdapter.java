package com.example.calllogger.adapter;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001:\u0002\u000e\u000fB\u0005\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0016J\u0018\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\tH\u0016\u00a8\u0006\u0010"}, d2 = {"Lcom/example/calllogger/adapter/CallLogAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/example/calllogger/data/CallLogEntity;", "Lcom/example/calllogger/adapter/CallLogAdapter$CallLogViewHolder;", "()V", "onBindViewHolder", "", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "CallLogDiffCallback", "CallLogViewHolder", "app_debug"})
public final class CallLogAdapter extends androidx.recyclerview.widget.ListAdapter<com.example.calllogger.data.CallLogEntity, com.example.calllogger.adapter.CallLogAdapter.CallLogViewHolder> {
    
    public CallLogAdapter() {
        super(null);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.example.calllogger.adapter.CallLogAdapter.CallLogViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.calllogger.adapter.CallLogAdapter.CallLogViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0002H\u0016\u00a8\u0006\t"}, d2 = {"Lcom/example/calllogger/adapter/CallLogAdapter$CallLogDiffCallback;", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/example/calllogger/data/CallLogEntity;", "()V", "areContentsTheSame", "", "oldItem", "newItem", "areItemsTheSame", "app_debug"})
    public static final class CallLogDiffCallback extends androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.calllogger.data.CallLogEntity> {
        
        public CallLogDiffCallback() {
            super();
        }
        
        @java.lang.Override()
        public boolean areItemsTheSame(@org.jetbrains.annotations.NotNull()
        com.example.calllogger.data.CallLogEntity oldItem, @org.jetbrains.annotations.NotNull()
        com.example.calllogger.data.CallLogEntity newItem) {
            return false;
        }
        
        @java.lang.Override()
        public boolean areContentsTheSame(@org.jetbrains.annotations.NotNull()
        com.example.calllogger.data.CallLogEntity oldItem, @org.jetbrains.annotations.NotNull()
        com.example.calllogger.data.CallLogEntity newItem) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bJ\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u000fH\u0002J\u0010\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\rH\u0002J\u0012\u0010\u0014\u001a\u00020\r2\b\u0010\u0015\u001a\u0004\u0018\u00010\rH\u0002J\u0010\u0010\u0016\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u0010\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u0010\u0010\u001a\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/example/calllogger/adapter/CallLogAdapter$CallLogViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/calllogger/databinding/ItemCallLogBinding;", "(Lcom/example/calllogger/databinding/ItemCallLogBinding;)V", "dateFormat", "Ljava/text/SimpleDateFormat;", "timeFormat", "bind", "", "callLog", "Lcom/example/calllogger/data/CallLogEntity;", "formatDataUsage", "", "bytes", "", "formatDuration", "seconds", "formatPhoneNumber", "phoneNumber", "getSimCardName", "phoneAccountId", "setupCallStatus", "setupCallType", "callType", "", "setupSyncStatus", "app_debug"})
    public static final class CallLogViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.calllogger.databinding.ItemCallLogBinding binding = null;
        @org.jetbrains.annotations.NotNull()
        private final java.text.SimpleDateFormat timeFormat = null;
        @org.jetbrains.annotations.NotNull()
        private final java.text.SimpleDateFormat dateFormat = null;
        
        public CallLogViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.calllogger.databinding.ItemCallLogBinding binding) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.example.calllogger.data.CallLogEntity callLog) {
        }
        
        private final void setupCallType(int callType) {
        }
        
        private final void setupSyncStatus(com.example.calllogger.data.CallLogEntity callLog) {
        }
        
        private final void setupCallStatus(com.example.calllogger.data.CallLogEntity callLog) {
        }
        
        private final java.lang.String formatPhoneNumber(java.lang.String phoneNumber) {
            return null;
        }
        
        private final java.lang.String formatDuration(long seconds) {
            return null;
        }
        
        private final java.lang.String formatDataUsage(long bytes) {
            return null;
        }
        
        private final java.lang.String getSimCardName(java.lang.String phoneAccountId) {
            return null;
        }
    }
}