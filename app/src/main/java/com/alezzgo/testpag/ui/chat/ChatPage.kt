package com.alezzgo.testpag.ui.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.alezzgo.testpag.ui.chat.ChatAction.InputTextChanged
import com.alezzgo.testpag.ui.composables.MessageCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.shareIn

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun ChatPage(
    chatState: ChatState,
    chatEvents: Flow<ChatEvent>,
    onAction: (ChatAction) -> Unit
) {
    val listState = rememberLazyListState()
    val uiScope = rememberCoroutineScope()

    ScrollToLaunchedEffect(listState,chatEvents,uiScope)
    FirstVisibleItemChangedLaunchedEffect(listState, onAction)

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(reverseLayout = true, modifier = Modifier.weight(1f), state = listState) {
            items(chatState.messages, key = { item -> item.id }) { message ->
                MessageCard(modifier = Modifier.animateItemPlacement(), message)
            }
        }

        SendPanel(inputText = chatState.inputText, onAction = onAction)
        Spacer(modifier = Modifier.size(32.dp))
    }
}

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun ScrollToLaunchedEffect(
    listState: LazyListState,
    chatEvents: Flow<ChatEvent>,
    uiScope: CoroutineScope
) {
    //todo
    chatEvents.onEach { chatEvent ->
        when (chatEvent) {
            is ChatEvent.ScrollTo -> listState.animateScrollToItem(0)
        }
    }.shareIn(uiScope, replay = 0, started = SharingStarted.WhileSubscribed())
}

@Composable
fun <T> ObserveAsEvents(flow : Flow<T>, onEvent: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow,lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            flow.collect(onEvent)
        }
    }
}

@Composable
fun FirstVisibleItemChangedLaunchedEffect(
    listState: LazyListState,
    onAction: (ChatAction) -> Unit
) {
    //todo Разобраться как точно работает под капотом
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.distinctUntilChanged().sample(300).collect { firstVisibleItemIndex ->
            onAction.invoke(ChatAction.FirstVisibleItemChanged(firstVisibleItemIndex))
        }
    }
}

@Composable
private fun SendPanel(
    modifier: Modifier = Modifier,
    inputText: String,
    onAction: (ChatAction) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = inputText,
            onValueChange = { value -> onAction.invoke(InputTextChanged(value)) })
        IconButton(onClick = { onAction.invoke(ChatAction.SendMessage) }) {
            Icon(imageVector = Icons.AutoMirrored.Rounded.Send, contentDescription = null)
        }
    }
}