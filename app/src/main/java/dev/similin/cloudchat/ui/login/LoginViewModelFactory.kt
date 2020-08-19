package dev.similin.cloudchat.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.similin.cloudchat.repository.LoginRepository

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val repo: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}