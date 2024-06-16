package com.alezzgo.testpag.ui.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alezzgo.testpag.core.ui.ObserveAsEvents
import com.alezzgo.testpag.ui.chat.ChatAction.InputTextChanged
import com.alezzgo.testpag.ui.composables.MessageCard
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun ChatPage(
    navController: NavController,
    chatState: ChatState,
    chatEvents: Flow<ChatEvent>,
    onAction: (ChatAction) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    ObserveAsEvents(flow = chatEvents) { event ->
        Log.d("ChatPage","ObserveAsEvents() event=$event")
        when(event){
            is ChatEvent.ScrollTo -> coroutineScope.launch { listState.animateScrollToItem(event.index) }
            is ChatEvent.NavigateToMessageDetails -> navController.navigate(Screen.MessageDetails(event.messageId))
        }
    }
    FirstVisibleItemChangedNotifier(listState,onAction)

    Scaffold {
        Column(modifier = Modifier.padding(it)) {

            //todo: need fix logger spam:
            //todo: updateAcquireFence: Did not find frame.
            //todo: Unable to acquire a buffer item, very likely client tried to acquire more than maxImages buffers

            //todo animate smooth appear from bottom

            LazyColumn(reverseLayout = true, modifier = Modifier.weight(1f), state = listState) {
                items(chatState.messages, key = { item -> item.id }) { message ->
                    MessageCard(modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null), message, onAction = onAction)
                }
            }

            SendPanel(inputText = chatState.inputText, onAction = onAction)
            Spacer(modifier = Modifier.size(32.dp))
        }
    }

}

@OptIn(FlowPreview::class)
@Composable
fun FirstVisibleItemChangedNotifier(
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
    //todo как вариант взять то что ниже, ток добавить тротлер
//    val firstVisibleItemIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }
//
//    LaunchedEffect(firstVisibleItemIndex.value) {
//        onAction.invoke(ChatAction.FirstVisibleItemChanged(firstVisibleItemIndex.value))
//    }
}

@Composable
private fun SendPanel(
    modifier: Modifier = Modifier,
    inputText: String,
    onAction: (ChatAction) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = inputText,
            onValueChange = { value -> onAction.invoke(InputTextChanged(value)) }
        )
        IconButton(onClick = { onAction.invoke(ChatAction.SendMessage) }) {
            Icon(imageVector = Icons.AutoMirrored.Rounded.Send, contentDescription = null)
        }
    }
}