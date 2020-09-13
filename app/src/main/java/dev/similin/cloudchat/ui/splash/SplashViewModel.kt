package dev.similin.cloudchat.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dev.similin.cloudchat.repository.SplashRepository

class SplashViewModel @ViewModelInject constructor(private val repo: SplashRepository) : ViewModel() {
    var uid: String? = null
    fun getUserID(): String? = repo.getUserID()
}