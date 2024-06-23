package com.alezzgo.testpag.ui.chatdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import javax.inject.Inject

class MessageDetailsViewModel @Inject constructor(
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
    companion object{
        private const val TAG = "MessageDetailsViewModel"
    }

    init {
        Log.d(TAG,"mId=${savedStateHandle.get<Long>("messageId")}}")
    }

}
