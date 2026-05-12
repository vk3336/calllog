package com.example.calllogger.network

import com.google.gson.GsonBuilder
import okhttp3.ConnectionPool
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

    // Cached service instances — keyed by "baseUrl|apiKey"
    // Same URL + key = same OkHttpClient + connection pool reused across all requests
    private val serviceCache = mutableMapOf<String, EspoApiService>()

    // Single shared OkHttpClient with a connection pool
    // Keeps up to 5 connections alive for 10 minutes of idle time
    private val sharedHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectionPool(ConnectionPool(5, 10, TimeUnit.MINUTES))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    /**
     * Returns a cached EspoApiService for the given URL + API key.
     * If the same URL and key are used again, the existing instance (and its
     * underlying HTTP connection pool) is reused — no new TCP/TLS handshake needed.
     */
    fun createEspoApiService(baseUrl: String, apiKey: String): EspoApiService {
        val cacheKey = "$baseUrl|$apiKey"
        return serviceCache.getOrPut(cacheKey) {
            // Add auth header interceptor on top of the shared client
            val authClient = sharedHttpClient.newBuilder()
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("X-Api-Key", apiKey)
                        .header("Content-Type", "application/json")
                        .build()
                    chain.proceed(request)
                })
                .build()

            val gson = GsonBuilder().create()

            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(authClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(EspoApiService::class.java)
        }
    }

    /** Call this if the URL or API key changes so the old cached service is discarded. */
    fun clearCache() {
        serviceCache.clear()
    }
}