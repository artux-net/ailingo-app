package org.ailingo.app.features.chat.presentation.desktop

import androidx.compose.foundation.layout.fillMaxSize
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
fun ChatScreenDesktop(
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
            BottomUserMessageBox(
                userTextField = userTextField,
                voiceToTextParser = voiceToTextParser,
                voiceState = voiceState,
                messages = chatState,
                listState = listState,
                onMessageSent = onMessageSent,
                isActiveJob = isActiveJob.value,
                onChatTextField = onChatTextField
            )
        }
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(chatState) { message ->
                MessageItemDesktop(message)
            }
        }
    }
}
