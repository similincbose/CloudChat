package dev.similin.cloudchat.ui.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dev.similin.cloudchat.repository.ChatRepository


class ChatViewModel @ViewModelInject constructor(private val repo: ChatRepository) : ViewModel() {
    var chatWithUser: String? = null
    var chatWithUserName: String? = null
    var chatMessage: String? = null

    fun getUserPhone(): String? = repo.getUserPhoneNumber()
}