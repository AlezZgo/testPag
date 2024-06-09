package com.alezzgo.testpag.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezzgo.testpag.core.cachedList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.sample
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val cachedListState = MutableStateFlow(cachedList)
    val middleVisibleItemIndexState = MutableStateFlow(0)

    init {
        combine(
            cachedListState.sample(1000),
            middleVisibleItemIndexState.sample(1000)
        ) { cachedList, middleVisibleItemIndex ->
            println("middle element: $middleVisibleItemIndex")

//            if (cachedList.size - middleVisibleItemIndex <= 20) {
//                cachedListState.emit(cachedList)
//                println("loaded 50 elements")
//            }
        }.launchIn(viewModelScope)
    }


}
