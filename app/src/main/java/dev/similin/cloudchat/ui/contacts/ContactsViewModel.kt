package dev.similin.cloudchat.ui.contacts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dev.similin.cloudchat.network.Resource
import dev.similin.cloudchat.repository.ContactRepository
import timber.log.Timber

class ContactsViewModel @ViewModelInject constructor(private val repo: ContactRepository) : ViewModel() {
    val contacts = mutableListOf<ContactsModel>()

    fun fetchUsers() = liveData {
        emit(Resource.loading())
        try {
            val response = repo.fetchUsers()
            emit(Resource.success(response))
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource.error(e.message ?: "Error Occurred"))
        }
    }
}