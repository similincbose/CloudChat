package dev.similin.cloudchat

import android.app.Application
import dev.similin.cloudchat.network.ChatApi
import dev.similin.cloudchat.network.RetrofitClient
import dev.similin.cloudchat.preference.ChatPreference
import dev.similin.cloudchat.repository.ContactRepository
import dev.similin.cloudchat.repository.LoginRepository
import dev.similin.cloudchat.repository.SplashRepository
import timber.log.Timber

class CloudChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    private fun getRetrofitClient(): ChatApi {
        return RetrofitClient().retrofit.create(ChatApi::class.java)
    }

    private fun getPreference(): ChatPreference {
        return ChatPreference(applicationContext)
    }

    fun getLoginRepository(): LoginRepository {
        return LoginRepository(getPreference(), getRetrofitClient())
    }

    fun getSplashRepository(): SplashRepository {
        return SplashRepository(getPreference())
    }

    fun getContactsRepository():ContactRepository{
        return  ContactRepository()
    }
}