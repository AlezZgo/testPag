package com.alezzgo.testpag.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.alezzgo.testpag.core.cachedList
import com.alezzgo.testpag.ui.composables.MessageCard
import com.alezzgo.testpag.ui.model.Message
import com.alezzgo.testpag.ui.theme.TestPagTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextUInt

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val itemList = viewModel.cachedListState.collectAsState()

            TestPagTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    CardsPage(itemList.value, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
    @Composable
    fun CardsPage(items: List<Message>, modifier: Modifier) {
        val listState = rememberLazyListState()

        LaunchedEffect(listState) {
            snapshotFlow {
                val visibleItems = listState.layoutInfo.visibleItemsInfo
                println("first visible item: ${visibleItems.firstOrNull()?.key ?: "null"} with index ${visibleItems.firstOrNull()?.index ?: "null"}")

                val middleIndex = visibleItems
                    .map { it.index }
                    .toList()[visibleItems.size / 2]

                middleIndex
            }.distinctUntilChanged().collect { middleIndex ->
                viewModel.middleVisibleItemIndexState.emit(middleIndex)
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier.weight(1f),
                state = listState,
            ) {
                items(items, key = { item -> item.id }) { item ->
                    MessageCard(modifier = Modifier.animateItemPlacement(), item)
                }
            }


            LaunchedEffect(items) {
                println("firstVisibleItemIndex ${Random.nextLong()} from launched effect: ${listState.firstVisibleItemIndex}")
                println("content of zero: ${items[0].content}")
                if (listState.firstVisibleItemIndex == 1) {
                    listState.animateScrollToItem(0)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(modifier = Modifier.weight(1f), onClick = {
                    lifecycleScope.launch {
                        viewModel.cachedListState.emit(
                            listOf(Message.random()) + viewModel.cachedListState.value
                        )
                    }
                }) {
                    Text("Add")
                }
                Button(modifier = Modifier.weight(1f), onClick = {
                    lifecycleScope.launch {
                        viewModel.cachedListState.emit(
                            viewModel.cachedListState.value.drop(1)
                        )

                    }
                }) {
                    Text("Remove")
                }
            }
            Spacer(modifier = Modifier.size(48.dp))

        }

    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewCardsPage() {
        TestPagTheme {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
            ) { innerPadding ->
                CardsPage(cachedList, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}