package com.alezzgo.testpag.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.alezzgo.testpag.ui.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val cachedList = (0..20).map { Message.random() }

    var chatState by mutableStateOf(ChatState(cachedList))
        private set

    init {

    }

    fun onAction(action: ChatAction) {
        when (action) {
            ChatAction.FirstVisibleItemChanged -> TODO()
            ChatAction.SendMessage -> TODO()
        }
    }


}
