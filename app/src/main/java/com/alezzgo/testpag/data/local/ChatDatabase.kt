package com.alezzgo.testpag.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alezzgo.testpag.data.local.entities.ChatEntity
import com.alezzgo.testpag.data.local.entities.MessageEntity

const val DB_VERSION = 1

@Database(entities = [ChatEntity::class, MessageEntity::class],version = DB_VERSION)
abstract class ChatDatabase : RoomDatabase() {

    abstract val dao : ChatDao
}