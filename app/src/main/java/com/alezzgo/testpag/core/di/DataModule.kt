package com.alezzgo.testpag.core.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.alezzgo.testpag.data.local.ChatDao
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
    fun provideDb(@ApplicationContext applicationContext: Context): ChatDatabase =
        Room.databaseBuilder(
            applicationContext,
            ChatDatabase::class.java,
            "chat-database"
        ).fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideChatDao(db: ChatDatabase) = db.chatDao

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext applicationContext: Context)
        = WorkManager.getInstance(applicationContext)


}