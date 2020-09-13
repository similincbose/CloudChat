package dev.similin.cloudchat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.similin.cloudchat.network.ChatApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideCloudApi(): ChatApi {
        return Retrofit.Builder()
            .baseUrl("https://myprojects-9c5a7.firebaseio.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ChatApi::class.java)
    }
}