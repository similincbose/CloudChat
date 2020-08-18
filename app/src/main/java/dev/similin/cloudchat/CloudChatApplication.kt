package dev.similin.cloudchat

import android.app.Application
import dev.similin.cloudchat.repository.LoginRepository

class CloudChatApplication :Application(){
    override fun onCreate() {
        super.onCreate()
    }

    fun getLoginRepository():LoginRepository{
        return LoginRepository()
    }
}