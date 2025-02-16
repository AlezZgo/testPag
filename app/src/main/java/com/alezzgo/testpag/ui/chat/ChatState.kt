package com.alezzgo.testpag.ui.chat

import com.alezzgo.testpag.data.local.models.ChatState

data class ChatUiState(
    val dbState : ChatState,
    val inputText : String
)

sealed interface ChatEffect {
    data class ScrollTo(val index: Int) : ChatEffect
    data class NavigateToMessageDetails(val messageId: Long) : ChatEffect
}

sealed interface ChatAction {
    data class FirstVisibleIndexChanged(val index: Int) : ChatAction
    data object SendMessage : ChatAction
    data class InputTextChanged(val text: String) : ChatAction
    data class OnMessageClick(val messageId: Long) : ChatAction
}

sealed interface ChatEvent {
//    data object SendMessage : ChatEvent
}