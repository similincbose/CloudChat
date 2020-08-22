package dev.similin.cloudchat.ui.splash

import androidx.lifecycle.ViewModel
import dev.similin.cloudchat.repository.SplashRepository

class SplashViewModel(private val repo: SplashRepository) : ViewModel() {
    var uid: String? = null
    fun getUserID(): String? = repo.getUserID()
}