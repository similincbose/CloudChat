package dev.similin.cloudchat.ui.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.similin.cloudchat.repository.ChatRepository

class ChatViewModel @ViewModelInject constructor(private val repo: ChatRepository) : ViewModel() {
    var chatWithUser: String? = null
    var chatWithUserName: String? = null
    var chatMessage: String? = null
    private val chatUserReference =
        Firebase.database.reference.child(repo.getUserPhoneNumber() + "_" + chatWithUser)

    private val chatWithReference =
        Firebase.database.reference.child(chatWithUser + "_" + repo.getUserPhoneNumber())

    fun addChatReference() = liveData {
        val map: MutableMap<String, String> = HashMap()
        var mapChat: Map<String, Object>? = null
        map["message"] = chatMessage.toString()
        map["user"] = chatWithUser.toString()
        chatUserReference.push().setValue(map)
        chatWithReference.push().setValue(map)
        var flag = false
        chatUserReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                mapChat = dataSnapshot.value as Map<String, Object>
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(error: DatabaseError) {
            }
        })
        emit(mapChat)
    }

    fun getUserPhone(): String? = repo.getUserPhoneNumber()
}