package dev.similin.cloudchat.preference

import android.content.Context
import androidx.core.content.edit
import dev.similin.cloudchat.util.COUNTRY_CODE
import dev.similin.cloudchat.util.UID

class ChatPreference(context: Context) {
    private val preference = context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)

    fun saveCountryCode(code: String) {
        preference.edit { putString(COUNTRY_CODE, code) }
    }

    fun getCountryCode(): String? {
        return preference.getString(COUNTRY_CODE, "")
    }

    fun saveUserID(uid: String) {
        preference.edit { putString(UID, uid) }
    }

    fun getUserID(): String? {
        return preference.getString(UID, "")
    }
}