package dev.similin.cloudchat.repository

import dev.similin.cloudchat.preference.ChatPreference

class LoginRepository(private val prefs: ChatPreference) {
    fun saveCountryCode(countryCode: String) {
        prefs.saveCountryCode(countryCode)
    }

    fun getCountryCode(): String? {
        return prefs.getCountryCode()
    }

    fun saveUserID(uid: String) {
        prefs.saveUserID(uid)
    }

    fun getUserID(): String? {
        return prefs.getUserID()
    }
}