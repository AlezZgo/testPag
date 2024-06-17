package com.alezzgo.testpag.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.alezzgo.testpag.ui.theme.TestPagTheme
import com.alezzgo.testpag.ui.chat.Screen.*
import com.alezzgo.testpag.ui.chatdetails.ChatDetailsPage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            TestPagTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Chat){
                    composable<Chat> {
                        val chatState = viewModel.chatState
                        val chatEvents = viewModel.chatEffects
                        ChatPage(navController,chatState, chatEvents, onAction = viewModel::onAction)
                    }
                    composable<MessageDetails> {
                        val args = it.toRoute<MessageDetails>()
                        ChatDetailsPage(args.messageId)
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