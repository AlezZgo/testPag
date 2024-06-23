package com.alezzgo.testpag.core

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.alezzgo.testpag.core.Screen.Chat
import com.alezzgo.testpag.core.Screen.MessageDetails
import com.alezzgo.testpag.ui.chat.ChatPage
import com.alezzgo.testpag.ui.chat.ChatViewModel
import com.alezzgo.testpag.ui.chatdetails.ChatDetailsPage
import com.alezzgo.testpag.ui.theme.TestPagTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            TestPagTheme {
                //todo inject somehow
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Chat) {
                    composable<Chat> {
                        val viewModel = hiltViewModel<ChatViewModel>()

                        val chatState = viewModel.chatState
                        val chatEvents = viewModel.chatEffects
                        ChatPage(
                            navController = navController,
                            chatState = chatState,
                            chatEvents = chatEvents,
                            onAction = viewModel::onAction
                        )
                    }
                    composable<MessageDetails> {
                        val args = it.toRoute<MessageDetails>()
                        ChatDetailsPage(id = args.messageId)
                    }
                }
            }
        }
    }
}

interface Screen {
    @Serializable
    data object Chat : Screen

    @Serializable
    data class MessageDetails(val messageId: Long) : Screen

}