package com.example.calllogger.network

import retrofit2.Response
import retrofit2.http.*

interface EspoApiService {
    
    @POST("Call")
    suspend fun createCall(@Body callData: EspoCallRequest): Response<EspoCallResponse>
    
    @GET("Call/{id}")
    suspend fun getCall(@Path("id") id: String): Response<EspoCallResponse>
    
    @PUT("Call/{id}")
    suspend fun updateCall(
        @Path("id") id: String,
        @Body callData: EspoCallRequest
    ): Response<EspoCallResponse>
}

data class EspoCallRequest(
    val name: String,
    val phoneNumber: String,
    val direction: String, // "Inbound" or "Outbound"
    val status: String, // "Held", "Not Held"
    val dateStart: String, // ISO format
    val duration: Int, // in seconds
    val description: String? = null,
    val contactName: String? = null
)

data class EspoCallResponse(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val direction: String,
    val status: String,
    val dateStart: String,
    val duration: Int,
    val description: String?,
    val contactName: String?
)