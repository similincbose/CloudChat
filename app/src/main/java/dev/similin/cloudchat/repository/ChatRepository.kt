package dev.similin.cloudchat.repository

import dev.similin.cloudchat.network.ChatApi
import dev.similin.cloudchat.preference.ChatPreference
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val prefs: ChatPreference,
) {
    fun getUserID(): String? {
        return prefs.getUserID()
    }

    fun getUserPhoneNumber(): String? {
        return prefs.getUserPhoneNumber()
    }
}