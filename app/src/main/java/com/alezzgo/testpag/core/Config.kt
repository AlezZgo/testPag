package com.alezzgo.testpag.core

import com.alezzgo.testpag.ui.model.Message

val cachedList = (0..150).map {
    Message.random()
}