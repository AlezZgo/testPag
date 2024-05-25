package com.alezzgo.testpag

val cachedList = (0..5).map {
    Message.random().copy(id = it.toLong())
}