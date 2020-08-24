package dev.similin.cloudchat.network

import dev.similin.cloudchat.model.UserResponseApi
import retrofit2.Response
import retrofit2.http.GET

interface ChatApi {
    @GET("users.json")
    suspend fun getUsers(): Response<UserResponseApi.UserResponse>
}