package dev.similin.cloudchat.repository

import dev.similin.cloudchat.network.ChatApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(private val api: ChatApi) {
    suspend fun fetchUsers() = api.getUsers()
}