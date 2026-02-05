package com.example.calllogger.network;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J!\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J!\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\t\u001a\u00020\nH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ+\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\r\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000e"}, d2 = {"Lcom/example/calllogger/network/EspoApiService;", "", "createCall", "Lretrofit2/Response;", "Lcom/example/calllogger/network/EspoCallResponse;", "callData", "Lcom/example/calllogger/network/EspoCallRequest;", "(Lcom/example/calllogger/network/EspoCallRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCall", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCall", "(Ljava/lang/String;Lcom/example/calllogger/network/EspoCallRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface EspoApiService {
    
    @retrofit2.http.POST(value = "Call")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createCall(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.calllogger.network.EspoCallRequest callData, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.calllogger.network.EspoCallResponse>> $completion);
    
    @retrofit2.http.GET(value = "Call/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCall(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.calllogger.network.EspoCallResponse>> $completion);
    
    @retrofit2.http.PUT(value = "Call/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateCall(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.calllogger.network.EspoCallRequest callData, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.calllogger.network.EspoCallResponse>> $completion);
}