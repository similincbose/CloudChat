package dev.similin.cloudchat.ui.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.similin.cloudchat.repository.ContactRepository

class ContactsViewModel(private val repo: ContactRepository) : ViewModel() {
    val contacts = mutableListOf<ContactsModel>()
}