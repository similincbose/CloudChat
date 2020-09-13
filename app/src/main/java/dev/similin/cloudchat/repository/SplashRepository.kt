package dev.similin.cloudchat.repository

import dagger.hilt.android.AndroidEntryPoint
import dev.similin.cloudchat.preference.ChatPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashRepository @Inject constructor(private val prefs: ChatPreference) {
    fun getUserID(): String? {
        return prefs.getUserID()
    }
}