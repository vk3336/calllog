package com.example.calllogger.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor() {
    
    companion object {
        @Volatile
        private var INSTANCE: RetrofitClient? = null
        
        fun getInstance(): RetrofitClient {
            return INSTANCE ?: synchronized(this) {
                val instance = RetrofitClient()
                INSTANCE = instance
                instance
            }
        }
    }
    
    fun createEspoApiService(baseUrl: String, apiKey: String): EspoApiService {
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val authenticatedRequest = originalRequest.newBuilder()
                .header("X-Api-Key", apiKey)
                .header("Content-Type", "application/json")
                .build()
            chain.proceed(authenticatedRequest)
        }
        
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        return retrofit.create(EspoApiService::class.java)
    }
}