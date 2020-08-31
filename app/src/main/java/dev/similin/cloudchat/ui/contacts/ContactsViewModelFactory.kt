package dev.similin.cloudchat.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.similin.cloudchat.repository.ContactRepository
import dev.similin.cloudchat.repository.LoginRepository
import dev.similin.cloudchat.ui.login.LoginViewModel

@Suppress("UNCHECKED_CAST")
class ContactsViewModelFactory(private val repo: ContactRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            return ContactsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}