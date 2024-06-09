package com.alezzgo.testpag.ui.chat

sealed interface ChatAction {
    data object FirstVisibleItemChanged : ChatAction

    data object SendMessage : ChatAction

    data class OnSendPanelInputChanged(val value: String) : ChatAction
}