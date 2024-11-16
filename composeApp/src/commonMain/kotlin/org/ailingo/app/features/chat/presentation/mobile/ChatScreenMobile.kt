package org.ailingo.app.features.chat.presentation.mobile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import org.ailingo.app.core.utils.voice.VoiceStates
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.features.chat.data.model.Message

@Composable
fun ChatScreenMobile(
    voiceToTextParser: VoiceToTextParser,
    userTextField: String,
    chatState: List<Message>,
    listState: LazyListState,
    voiceState: State<VoiceStates>,
    isActiveJob: State<Boolean>,
    onMessageSent: (String) -> Unit,
    onChatTextField: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomUserMessageBoxMobile(
                userTextField,
                voiceToTextParser,
                voiceState,
                chatState,
                listState,
                isActiveJob.value,
                onMessageSent = onMessageSent,
                onChatTextField = onChatTextField
            )
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(chatState) { message ->
                MessageItemMobile(message)
            }
        }
    }
}
