package com.alezzgo.testpag.ui.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezzgo.testpag.ui.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val cachedList = (0..200).map { Message.random() }
    val TAG = "MainViewModel"
    var chatState by mutableStateOf(ChatState(cachedList,""))
        private set

    private val _chatEvents = Channel<ChatEvent>()
    val chatEvents = _chatEvents.receiveAsFlow()

    fun onAction(action: ChatAction) {
        Log.d(TAG,"onAction() action=$action")
        when (action) {
            is ChatAction.SendMessage -> sendMessage()
            is ChatAction.FirstVisibleItemChanged -> {  }
            is ChatAction.InputTextChanged -> chatState = chatState.copy(inputText = action.value)
            is ChatAction.OnMessageClick -> _chatEvents.trySend(ChatEvent.NavigateToMessageDetails(action.messageId))
        }
    }

    private fun sendMessage(){
        chatState = chatState.copy(
            messages = listOf(Message(Random.nextLong(),chatState.inputText)) + chatState.messages,
            inputText = ""
        )
        _chatEvents.trySend(ChatEvent.ScrollTo(0))
    }

}
