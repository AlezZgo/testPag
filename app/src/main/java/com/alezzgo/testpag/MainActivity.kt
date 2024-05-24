package com.alezzgo.testpag

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alezzgo.testpag.ui.theme.TestPagTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextUInt

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    val cachedListState = MutableStateFlow(cachedList)
    val middleVisibleItemIndexState = MutableStateFlow(0)

    //emulate fetching data from network
    val fetchChunk = {
        mutableListOf<String>().apply {
            repeat(50) {
                add(Random.nextUInt(1000u).toString())
            }
        }.toList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        println(viewModel.str)
        setContent {
            val itemList = cachedListState.collectAsState()

            TestPagTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) { innerPadding ->
                    CardsPage(itemList.value, modifier = Modifier.padding(innerPadding))
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            combine(
                cachedListState,
                middleVisibleItemIndexState
            ) { cachedList, middleVisibleItemIndex ->
                println("middle element: $middleVisibleItemIndex")

                if (cachedList.size - middleVisibleItemIndex <= 20) {
                    cachedListState.emit(cachedList.plus(fetchChunk.invoke()))
                    println("loaded 50 elements")
                }
            }.collect()
        }
    }


    @Composable
    fun CardsPage(items: List<String>, modifier: Modifier) {
        val listState = rememberLazyListState()

        LaunchedEffect(listState) {
            snapshotFlow {
                val visibleItems = listState.layoutInfo.visibleItemsInfo

                val middleIndex = visibleItems
                    .map { it.index }
                    .toList()[visibleItems.size / 2]

                middleIndex
            }.collectLatest { middleIndex ->
                middleVisibleItemIndexState.emit(middleIndex)
            }
        }

        LazyColumn(
            modifier = modifier,
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            listState.scrollToItem(40)
                        }
                    }
                ) {
                    Text(text = "Scroll to 40")
                }
            }
            items(items) { item ->
                Card(item)
            }
        }
    }

    @Composable
    fun Card(name: String) {
        OutlinedCard(modifier = Modifier
            .fillMaxWidth()
            .height(48.dp), onClick = { /*TODO*/ }) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Item: $name"
            )
        }
    }
}