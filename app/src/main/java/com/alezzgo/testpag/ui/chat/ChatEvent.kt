package com.alezzgo.testpag.ui.chat

sealed interface ChatEvent {
    data class ScrollTo(val index: Int) : ChatEvent
}