package dev.similin.cloudchat.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.similin.cloudchat.repository.SplashRepository

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(private val repo: SplashRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}