package com.alezzgo.testpag.ui.chatdetails

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ChatDetailsPage(id : Long) {
    Text(text = id.toString())
}