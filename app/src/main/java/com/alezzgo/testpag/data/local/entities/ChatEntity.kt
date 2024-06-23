package com.alezzgo.testpag.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatEntity(
    @PrimaryKey
    val id : Long,
    val text : String,
    val firstVisibleMessageId : Long?
)