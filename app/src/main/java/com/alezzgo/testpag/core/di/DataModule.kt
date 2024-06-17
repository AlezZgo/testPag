package com.alezzgo.testpag.core.di

import android.content.Context
import androidx.room.Room
import com.alezzgo.testpag.data.local.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext applicationContext : Context) = Room.databaseBuilder(
        applicationContext,
        ChatDatabase::class.java,
        "chat-database"
    ).build()

}