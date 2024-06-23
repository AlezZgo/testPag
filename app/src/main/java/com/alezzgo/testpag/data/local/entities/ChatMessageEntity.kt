package com.alezzgo.testpag.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatMessageEntity(
    @PrimaryKey
    val id : Long,
    val chatId : Long,
    val content: String,
    val timeStamp : Long
)