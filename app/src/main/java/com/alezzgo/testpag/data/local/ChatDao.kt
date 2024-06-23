package com.alezzgo.testpag.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.alezzgo.testpag.data.local.entities.ChatEntity
import com.alezzgo.testpag.data.local.entities.ChatMessageEntity
import com.alezzgo.testpag.data.local.models.ChatState
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Upsert
    suspend fun upsertChat(vararg chat: ChatEntity)

    @Delete
    suspend fun delete(vararg chat: ChatEntity)

    @Upsert
    suspend fun upsertMessage(vararg message: ChatMessageEntity)

    @Delete
    suspend fun delete(vararg message: ChatMessageEntity)

    @Transaction
    @Query("SELECT * FROM ChatEntity chat WHERE chat.id = :chatId")
    fun chat(chatId : Long): Flow<ChatState>

}