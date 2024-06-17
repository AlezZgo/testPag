package com.alezzgo.testpag.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.alezzgo.testpag.data.local.entities.ChatEntity
import com.alezzgo.testpag.data.local.models.Chat
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Upsert
    suspend fun upsert(vararg chat: ChatEntity)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(chat: Chat)

    @Delete
    suspend fun delete(vararg chat: ChatEntity)

    @Transaction
    @Query("SELECT * FROM ChatEntity")
    fun chats(): Flow<List<Chat>>
}