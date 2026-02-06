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
    val status: String = "Planned",
    val direction: String = "Outbound",
    val phone: String? = null,
    val cSeconds: String,
    val deleted: Boolean = false,
    val dateStart: String? = null,
    val duration: Int? = null,
    val reminders: List<Any> = emptyList(),
    val parentName: String? = null,
    val accountName: String? = null,
    val usersIds: List<String> = emptyList(),
    val usersNames: Map<String, Any> = emptyMap(),
    val usersColumns: Map<String, Any> = emptyMap(),
    val contactsIds: List<String> = emptyList(),
    val contactsNames: Map<String, Any> = emptyMap(),
    val contactsColumns: Map<String, Any> = emptyMap(),
    val leadsIds: List<String> = emptyList(),
    val leadsNames: Map<String, Any> = emptyMap(),
    val leadsColumns: Map<String, Any> = emptyMap()
)

data class EspoCallResponse(
    val id: String,
    val name: String,
    val deleted: Boolean = false,
    val status: String,
    val dateStart: String? = null,
    val duration: Int? = null, // API might return this
    val cSeconds: Int? = null, // Your API uses cSeconds for duration
    val reminders: List<Any> = emptyList(),
    val direction: String,
    val createdAt: String,
    val modifiedAt: String,
    val phoneNumbersMap: Map<String, Any> = emptyMap(),
    val parentName: String? = null,
    val accountName: String? = null,
    val usersIds: List<String> = emptyList(),
    val usersNames: Map<String, Any> = emptyMap(),
    val usersColumns: Map<String, Any> = emptyMap(),
    val contactsIds: List<String> = emptyList(),
    val contactsNames: Map<String, Any> = emptyMap(),
    val contactsColumns: Map<String, Any> = emptyMap(),
    val leadsIds: List<String> = emptyList(),
    val leadsNames: Map<String, Any> = emptyMap(),
    val leadsColumns: Map<String, Any> = emptyMap(),
    val createdById: String? = null,
    val createdByName: String? = null,
    val modifiedByName: String? = null,
    val assignedUserName: String? = null,
    val teamsIds: List<String> = emptyList(),
    val teamsNames: Map<String, Any> = emptyMap()
)