package dev.similin.cloudchat.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class PreferenceModule {
    @Provides
    @Singleton
    fun provideCloudPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
    }
}