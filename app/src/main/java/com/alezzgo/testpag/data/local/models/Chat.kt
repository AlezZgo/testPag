package com.alezzgo.testpag.data.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.alezzgo.testpag.data.local.entities.ChatEntity
import com.alezzgo.testpag.data.local.entities.MessageEntity

data class Chat (
    @Embedded val chat : ChatEntity,
    @Relation(parentColumn = "id",entityColumn = "chatId")
    val messages : List<MessageEntity>
)