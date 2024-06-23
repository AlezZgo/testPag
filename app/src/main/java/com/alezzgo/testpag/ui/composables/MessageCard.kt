package com.alezzgo.testpag.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.alezzgo.testpag.data.local.entities.ChatMessageEntity
import com.alezzgo.testpag.ui.chat.ChatAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageCard(modifier: Modifier = Modifier, message: ChatMessageEntity, onAction: (ChatAction) -> Unit) {

    val isClicked = remember {
        mutableStateOf(false)
    }

    OutlinedCard(modifier = modifier.fillMaxWidth().combinedClickable(
        onClick = { onAction.invoke(ChatAction.OnMessageClick(message.id)) },
        onLongClick = { isClicked.value = !isClicked.value }
    )){
        Row {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Item: ${message.content}"
            )
            AnimatedVisibility(visible = isClicked.value) {
                Text(text = "Show!!!")
            }

            val rotation = animateFloatAsState(targetValue = if (isClicked.value) 180f else 0f)

            Image(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                modifier = Modifier
                    .align(CenterVertically)
                    .graphicsLayer(
                        rotationZ = rotation.value
                    )
            )
        }


    }
}