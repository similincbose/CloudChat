package dev.similin.cloudchat.preference

import android.content.Context
import androidx.core.content.edit
import dev.similin.cloudchat.util.*

class ChatPreference(context: Context) {
    private val preference = context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)

    fun putString(data: String) {
        preference.edit { putString(COUNTRY_CODE, data) }
    }
}