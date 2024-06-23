package com.alezzgo.testpag.ui.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.alezzgo.testpag.ui.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    val cachedList = (0..200).map { Message.random() }
    val TAG = "MainViewModel"
    var chatState by mutableStateOf(ChatState(cachedList,""))
        private set

    //  todo think about mutableListStateOf<>
    private val _chatEffects = Channel<ChatEffect>()
    val chatEffects = _chatEffects.receiveAsFlow()

    fun onAction(action: ChatAction) {
        Log.d(TAG,"onAction() action=$action")
        when (action) {
            is ChatAction.SendMessage -> sendMessage()
            is ChatAction.FirstVisibleItemChanged -> {  }
            is ChatAction.InputTextChanged -> chatState = chatState.copy(inputText = action.value)
            is ChatAction.OnMessageClick -> _chatEffects.trySend(ChatEffect.NavigateToMessageDetails(action.messageId))
        }
    }

    private fun sendMessage(){
        chatState = chatState.copy(
            messages = listOf(Message(Random.nextLong(),chatState.inputText)) + chatState.messages,
            inputText = ""
        )
        _chatEffects.trySend(ChatEffect.ScrollTo(0))
    }

}