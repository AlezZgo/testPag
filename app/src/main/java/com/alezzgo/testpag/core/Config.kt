package com.alezzgo.testpag.core

import com.alezzgo.testpag.ui.chat.Message

val cachedList = (0..150).map {
    Message.random()
}