package com.alezzgo.testpag.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezzgo.testpag.ui.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val cachedList = (0..20).map { Message.random() }

    var chatState by mutableStateOf(ChatState(cachedList,""))
        private set

    private val _chatEvents = MutableSharedFlow<ChatEvent>()
    val chatEvents = _chatEvents.asSharedFlow()

    fun onAction(action: ChatAction) {
        when (action) {
            is ChatAction.FirstVisibleItemChanged -> TODO()
            is ChatAction.SendMessage -> sendMessage()
            is ChatAction.InputTextChanged -> chatState = chatState.copy(inputText = action.value)
        }
    }

    fun onEvent(event : ChatEvent){
        viewModelScope.launch {
            when(event){
                is ChatEvent.ScrollTo -> _chatEvents.emit(ChatEvent.ScrollTo(event.index))
            }
        }
    }

    private fun sendMessage(){
        chatState = chatState.copy(
            messages = listOf(Message(Random.nextLong(),chatState.inputText)) + chatState.messages,
            inputText = ""
        )
        onEvent(ChatEvent.ScrollTo(0))
    }


}
