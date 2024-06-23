package com.alezzgo.testpag.data.local.models

import androidx.room.Embedded
import androidx.room.Relation
import com.alezzgo.testpag.data.local.entities.ChatEntity
import com.alezzgo.testpag.data.local.entities.ChatMessageEntity

data class ChatState (
    @Embedded val chat : ChatEntity,
    @Relation(parentColumn = "id",entityColumn = "chatId")
    val messages : List<ChatMessageEntity>
) {
    companion object {
        fun initial(id : Long) = ChatState(
            chat = ChatEntity(id = id, text = "", currentFirstVisibleMessageId = null),
            messages = emptyList()
        )
    }
}