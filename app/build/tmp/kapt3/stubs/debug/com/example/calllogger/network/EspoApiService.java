package com.example.calllogger.network;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\bf\u0018\u00002\u00020\u0001J!\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J!\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\t\u001a\u00020\nH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJS\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u00032\b\b\u0003\u0010\u000e\u001a\u00020\n2\b\b\u0003\u0010\u000f\u001a\u00020\n2\b\b\u0001\u0010\u0010\u001a\u00020\n2\b\b\u0003\u0010\u0011\u001a\u00020\n2\b\b\u0003\u0010\u0012\u001a\u00020\n2\b\b\u0001\u0010\u0013\u001a\u00020\nH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0014J+\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0017"}, d2 = {"Lcom/example/calllogger/network/EspoApiService;", "", "createCall", "Lretrofit2/Response;", "Lcom/example/calllogger/network/EspoCallResponse;", "callData", "Lcom/example/calllogger/network/EspoCallRequest;", "(Lcom/example/calllogger/network/EspoCallRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCall", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchCalls", "Lcom/example/calllogger/network/EspoSearchResponse;", "type0", "attr0", "dateStart", "type1", "attr1", "cUserPhone", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCall", "(Ljava/lang/String;Lcom/example/calllogger/network/EspoCallRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
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
    
    @retrofit2.http.GET(value = "Call")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object searchCalls(@retrofit2.http.Query(value = "where[0][type]")
    @org.jetbrains.annotations.NotNull()
    java.lang.String type0, @retrofit2.http.Query(value = "where[0][attribute]")
    @org.jetbrains.annotations.NotNull()
    java.lang.String attr0, @retrofit2.http.Query(value = "where[0][value]")
    @org.jetbrains.annotations.NotNull()
    java.lang.String dateStart, @retrofit2.http.Query(value = "where[1][type]")
    @org.jetbrains.annotations.NotNull()
    java.lang.String type1, @retrofit2.http.Query(value = "where[1][attribute]")
    @org.jetbrains.annotations.NotNull()
    java.lang.String attr1, @retrofit2.http.Query(value = "where[1][value]")
    @org.jetbrains.annotations.NotNull()
    java.lang.String cUserPhone, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.calllogger.network.EspoSearchResponse>> $completion);
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}