package com.alezzgo.testpag.ui.chat

sealed interface ChatAction {
    data class FirstVisibleItemChanged(val index: Int) : ChatAction

    data object SendMessage : ChatAction

    data class InputTextChanged(val value: String) : ChatAction

    data class OnMessageClick(val messageId: Long) : ChatAction
}