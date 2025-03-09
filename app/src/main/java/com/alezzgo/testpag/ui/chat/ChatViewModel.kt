package com.alezzgo.testpag.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezzgo.testpag.data.local.ChatDao
import com.alezzgo.testpag.data.local.entities.ChatEntity
import com.alezzgo.testpag.data.local.entities.ChatMessageEntity
import com.alezzgo.testpag.data.local.entities.SendStatus
import com.alezzgo.testpag.data.local.models.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatDao: ChatDao,
) : ViewModel() {

    val TAG = "MainViewModel"

    private val inputText = MutableStateFlow<String?>("")

    val chatUiState = combine(
        chatDao.chat(0),
        inputText
    ) { dbChat: ChatState?, inputText: String? ->

        Log.d(TAG, "dbChat=$dbChat")

        //todo chat is always null!!
        if(dbChat==null){
            chatDao.upsertChat(ChatEntity(id = 0,"", 0))
            return@combine ChatUiState(ChatState.initial(id = 0), "")
        }

        val dbChatWithSortedMessages =
            dbChat.copy(messages = dbChat.messages.sortedByDescending { it.timeStamp })

        if (inputText == null) {
            ChatUiState(dbChatWithSortedMessages, dbChat.chat.draftText)
        } else {
            ChatUiState(dbChatWithSortedMessages, inputText)
        }
    }
    .catch { e -> Log.e(TAG, "Error in combine", e) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ChatUiState(ChatState.initial(id = 0), "")
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
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            chatDao.upsertMessage(
                ChatMessageEntity(
                    messageId = System.currentTimeMillis(),
                    chatId = chatUiState.value.dbState.chat.id,
                    content = chatUiState.value.inputText,
                    timeStamp = System.currentTimeMillis(),
                    sendStatus = SendStatus.SENDING
                )
            )
            chatDao.upsertChat(chatUiState.value.dbState.chat.copy(draftText = ""))
            inputText.update { "" }
            _chatEffects.send(ChatEffect.ScrollTo(0))
        }
    }

    private fun changeSendText(value: String) {
        Log.d(TAG, "changeSendText() value=$value")

        viewModelScope.launch {
            inputText.update { value }
        }
    }

    private fun changeFirstVisibleMessageId(currentIndex: Int) {
        viewModelScope.launch {
            chatDao.upsertChat(
                chatUiState.value.dbState.chat.copy(
                    firstVisibleMessageId = chatUiState.value.dbState.messages.getOrNull(currentIndex)?.messageId
                )
            )
        }
    }
}
