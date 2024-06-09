package com.alezzgo.testpag.ui.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.alezzgo.testpag.ui.composables.MessageCard
import com.alezzgo.testpag.ui.model.Message
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun ChatPage(chatState : ChatState, onAction: (ChatAction) -> Unit) {
    val listState = rememberLazyListState()

    ScrollToLaunchedEffect(listState)
    AutoScrollLaunchedEffect(listState)

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(reverseLayout = true,modifier = Modifier.weight(1f),state = listState) {
            items(chatState.messages, key = { item -> item.id }) { message ->
                MessageCard(modifier = Modifier.animateItemPlacement(), message)
            }
        }

        SendPanel()
    }
}

@Composable
fun ScrollToLaunchedEffect(listState: LazyListState) {
    LaunchedEffect(listState) {
        snapshotFlow {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            println("first visible item: ${visibleItems.firstOrNull()?.key ?: "null"} with index ${visibleItems.firstOrNull()?.index ?: "null"}")

            val middleIndex = visibleItems
                .map { it.index }
                .toList()[visibleItems.size / 2]

            middleIndex
        }.distinctUntilChanged().collect { middleIndex ->
//            viewModel.middleVisibleItemIndexState.emit(middleIndex)
        }
    }
}

@Composable
fun AutoScrollLaunchedEffect(listState: LazyListState) {
    LaunchedEffect(listState) {
        if (listState.firstVisibleItemIndex == 1) {
            listState.animateScrollToItem(0)
        }
    }
}

@Composable
private fun SendPanel(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Button(modifier = Modifier.weight(1f), onClick = {
//            lifecycleScope.launch {
//                viewModel.cachedListState.emit(
//                    listOf(Message.random()) + viewModel.cachedListState.value
//                )
//            }
        }) {
            Text("Add")
        }
        Button(modifier = Modifier.weight(1f), onClick = {
//            lifecycleScope.launch {
//                viewModel.cachedListState.emit(
//                    viewModel.cachedListState.value.drop(1)
//                )
//
//            }
        }) {
            Text("Remove")
        }
    }
}