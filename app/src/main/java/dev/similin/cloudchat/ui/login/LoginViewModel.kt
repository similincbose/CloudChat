package dev.similin.cloudchat.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val phoneNumber=MutableLiveData<String>()
    val countryCode=MutableLiveData<String>()

}