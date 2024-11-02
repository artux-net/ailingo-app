package org.ailingo.app.feature_chat.presentation.desktop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import org.ailingo.app.core.helper_voice.VoiceStates
import org.ailingo.app.core.helper_voice.VoiceToTextParser
import org.ailingo.app.feature_chat.data.model.Message

@Composable
fun ChatScreenDesktop(
    voiceToTextParser: VoiceToTextParser,
    chatTextField: String,
    chatState: List<Message>,
    listState: LazyListState,
    voiceState: State<VoiceStates>,
    onMessageSent: (String) -> Unit,
    isActiveJob: State<Boolean>,
    onChatTextField: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomUserMessageBox(
                chatTextField = chatTextField,
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