package dev.similin.cloudchat.ui.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.similin.cloudchat.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ChatViewModel @ViewModelInject constructor(private val repo: ChatRepository) : ViewModel() {
    var chatWithUser: String? = null
    var chatWithUserName: String? = null
    var chatMessage: String? = null

    private val _mapChat= MutableLiveData<Map<String,String>>()
    val mapChat : LiveData<Map<String,String>>
        get() = _mapChat

    fun addChatReference() {
        viewModelScope.launch(Dispatchers.IO) {
            val chatUserReference =
                Firebase.database.reference.child("message").child(getUserPhone() + "_" + chatWithUser)

            val chatWithReference =
                Firebase.database.reference.child("message").child(chatWithUser + "_" + getUserPhone())
            val map: MutableMap<String, String> = HashMap()
            map["message"] = chatMessage.toString()
            map["user"] = chatWithUser.toString()
            chatUserReference.push().setValue(map)
            chatWithReference.push().setValue(map)
            chatUserReference.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    _mapChat.postValue(dataSnapshot.value as Map<String, String>)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    fun getChatReference() {
        viewModelScope.launch(Dispatchers.IO) {
            val chatUserReference =
                Firebase.database.reference.child("message").child(getUserPhone() + "_" + chatWithUser)

            val chatWithReference =
                Firebase.database.reference.child("message").child(chatWithUser + "_" + getUserPhone())
            chatUserReference.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    _mapChat.postValue(dataSnapshot.value as Map<String, String>)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    fun getUserPhone(): String? = repo.getUserPhoneNumber()
}