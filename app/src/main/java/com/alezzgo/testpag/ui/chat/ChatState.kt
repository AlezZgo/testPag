package com.alezzgo.testpag.ui.chat

import com.alezzgo.testpag.ui.model.Message

data class ChatState(
    val messages : List<Message>,
    val inputText : String
)

sealed interface ChatEffect {
    data class ScrollTo(val index: Int) : ChatEffect
    data class NavigateToMessageDetails(val messageId: Long) : ChatEffect
}

sealed interface ChatAction {
    data class FirstVisibleItemChanged(val index: Int) : ChatAction
    data object SendMessage : ChatAction
    data class InputTextChanged(val value: String) : ChatAction
    data class OnMessageClick(val messageId: Long) : ChatAction
}

sealed interface ChatEvent {
    data object SendMessage : ChatEvent

}