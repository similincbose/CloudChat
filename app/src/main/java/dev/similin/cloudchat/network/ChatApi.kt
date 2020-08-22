package dev.similin.cloudchat.network

import dev.similin.cloudchat.model.Users
import retrofit2.http.GET

interface ChatApi {
    @GET("users.json")
    suspend fun getUsers(): List<Users>
}