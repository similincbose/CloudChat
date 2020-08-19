package dev.similin.cloudchat.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.similin.cloudchat.repository.LoginRepository

class LoginViewModel(private val repo: LoginRepository) : ViewModel() {
    var phoneNumber: String? = null
    val clicked = MutableLiveData<Boolean>()
    var verificationInProgress = false
    var storedVerificationId: String? = null

    fun saveCountryCode(countryCode: String) = repo.saveCountryCode(countryCode)

    fun onLoginButtonClicked() {
        clicked.value = true
    }
}