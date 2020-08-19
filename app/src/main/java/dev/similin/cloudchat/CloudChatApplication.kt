package dev.similin.cloudchat

import android.app.Application
import dev.similin.cloudchat.preference.ChatPreference
import dev.similin.cloudchat.repository.LoginRepository

class CloudChatApplication : Application() {

    private fun getPreference(): ChatPreference {
        return ChatPreference(applicationContext)
    }

    fun getLoginRepository(): LoginRepository {
        return LoginRepository(getPreference())
    }
}