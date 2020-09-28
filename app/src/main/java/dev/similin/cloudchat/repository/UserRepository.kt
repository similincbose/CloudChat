package dev.similin.cloudchat.repository

import dev.similin.cloudchat.network.ChatApi
import dev.similin.cloudchat.preference.ChatPreference
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val prefs: ChatPreference,
    private val api: ChatApi
) {
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

    fun saveUserPhoneNumber(phone: String) {
        prefs.saveUserPhoneNumber(phone)
    }

    fun getUserPhoneNumber(): String? {
        return prefs.getUserPhoneNumber()
    }

    suspend fun fetchUsers() = api.getUsers()


}