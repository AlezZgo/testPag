package com.alezzgo.testpag.ui.model

import kotlin.random.Random
import kotlin.random.nextUInt

data class Message(val id: Long, val content: String) {
    companion object {
        fun random() = Message(Random.nextLong(), Random.nextUInt(1000u).toString())
    }
}

