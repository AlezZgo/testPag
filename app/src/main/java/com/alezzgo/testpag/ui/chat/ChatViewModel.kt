package com.alezzgo.testpag.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezzgo.testpag.data.local.ChatDao
import com.alezzgo.testpag.data.local.entities.ChatMessageEntity
import com.alezzgo.testpag.data.local.entities.SendStatus
import com.alezzgo.testpag.data.local.models.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatDao: ChatDao,
) : ViewModel() {

    val TAG = "MainViewModel"

    val chatState = chatDao.chat(0)
        .filterNotNull()
        .map { it.copy(messages = it.messages.sortedByDescending { it.timeStamp }) } //todo dao should return order by timestamp
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ChatState.initial(id = 0)
        )

    private val _chatEffects = Channel<ChatEffect>()
    val chatEffects = _chatEffects.receiveAsFlow()


    fun onAction(action: ChatAction) {
        Log.d(TAG, "onAction() action=$action")
        when (action) {
            is ChatAction.SendMessage -> sendMessage()
            is ChatAction.FirstVisibleIndexChanged -> changeFirstVisibleMessageId(action.index)
            is ChatAction.InputTextChanged -> changeSendText(action.text)
            is ChatAction.OnMessageClick -> _chatEffects.trySend(
                ChatEffect.NavigateToMessageDetails(
                    action.messageId
                )
            )
            else -> {}
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            chatDao.upsertMessage(
                ChatMessageEntity(
                    messageId = System.currentTimeMillis(),
                    chatId = chatState.value.chat.id,
                    content = chatState.value.chat.text,
                    timeStamp = System.currentTimeMillis(),
                    sendStatus = SendStatus.SENDING
                )
            )
            chatDao.upsertChat(chatState.value.chat.copy(text = ""))
            _chatEffects.send(ChatEffect.ScrollTo(0))
        }
    }

    private fun changeSendText(value: String) {
        viewModelScope.launch {
            chatDao.upsertChat(
                chatState.value.chat.copy(text = value)
            )
        }
    }

    private fun changeFirstVisibleMessageId(currentIndex: Int) {
        viewModelScope.launch {
            chatDao.upsertChat(
                chatState.value.chat.copy(
                    firstVisibleMessageId = chatState.value.messages.getOrNull(currentIndex)?.messageId
                )
            )
        }
    }
}
