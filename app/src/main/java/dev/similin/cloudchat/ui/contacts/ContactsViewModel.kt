package dev.similin.cloudchat.ui.contacts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dev.similin.cloudchat.network.Resource
import dev.similin.cloudchat.repository.ContactRepository
import timber.log.Timber

class ContactsViewModel @ViewModelInject constructor(private val repo: ContactRepository) :
    ViewModel() {

    val contacts = mutableListOf<ContactsModel>()

    private val _contactList = MutableLiveData<List<ContactsModel>>()
    val contactList: LiveData<List<ContactsModel>>
        get() = _contactList


    fun fetchUsers() = liveData {
        emit(Resource.loading())
        try {
            val response = repo.fetchUsers()
            val list = mutableListOf<ContactsModel>()
            response.body()?.users?.forEach { user ->
                contacts.forEach { contact ->
                    if (user.key == contact.contactNumber?.replace(" ", "")) {
                        list.add(ContactsModel(contact.contactNumber?.replace(" ", ""), contact.contactName))
                    }
                }
            }
            _contactList.value = list
            Timber.d(contactList.value.toString())
            emit(Resource.success(response))
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource.error(e.message ?: "Error Occurred", null))
        }
    }
}