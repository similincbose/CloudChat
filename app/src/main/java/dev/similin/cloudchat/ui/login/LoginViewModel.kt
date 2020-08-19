package dev.similin.cloudchat.ui.login

import androidx.lifecycle.ViewModel
import dev.similin.cloudchat.repository.LoginRepository

class LoginViewModel(private val repo: LoginRepository) : ViewModel() {
    var phoneNumber: String? = null

    fun saveCountryCode(countryCode: String) = repo.saveCountryCode(countryCode)

    fun loginButtonClickFunction() {

    }
}