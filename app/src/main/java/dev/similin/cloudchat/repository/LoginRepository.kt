package dev.similin.cloudchat.repository

import dev.similin.cloudchat.preference.ChatPreference

class LoginRepository(private val prefs: ChatPreference) {
    fun saveCountryCode(countryCode: String) {
        prefs.putString(countryCode)
    }

    fun getCountryCode(): String? {
        return prefs.getCountryCode()
    }
}