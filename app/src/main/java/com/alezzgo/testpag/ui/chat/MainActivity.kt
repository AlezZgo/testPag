package com.alezzgo.testpag.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.alezzgo.testpag.ui.theme.TestPagTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val chatState = viewModel.chatState
            val chatEvents = viewModel.chatEvents

            TestPagTheme {
//                val navController = rememberNavController()

//                Scaffold(
//                    modifier = Modifier
//                        .fillMaxSize()
//                ) {
                    ChatPage(chatState, chatEvents, onAction = viewModel::onAction)
//                }
            }
        }
    }
}