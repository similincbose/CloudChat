package dev.similin.cloudchat.repository

import dev.similin.cloudchat.preference.ChatPreference

class SplashRepository(private val prefs: ChatPreference) {
    fun getUserID(): String? {
        return prefs.getUserID()
    }
}