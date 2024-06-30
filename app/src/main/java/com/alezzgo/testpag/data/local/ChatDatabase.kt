package com.alezzgo.testpag.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alezzgo.testpag.data.local.entities.ChatEntity
import com.alezzgo.testpag.data.local.entities.ChatMessageEntity

const val DB_VERSION = 7

@Database(entities = [ChatEntity::class, ChatMessageEntity::class],version = DB_VERSION)
abstract class ChatDatabase : RoomDatabase() {

    abstract val chatDao : ChatDao
}