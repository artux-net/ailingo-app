package org.ailingo.app.features.chat.presentation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.core.utils.windowinfo.info.WindowInfo
import org.ailingo.app.features.chat.presentation.desktop.ChatScreenDesktop
import org.ailingo.app.features.chat.presentation.mobile.ChatScreenMobile

@Composable
fun ChatScreen(
    voiceToTextParser: VoiceToTextParser,
    windowInfo: WindowInfo,
    login: String,
    password: String
) {
    val voiceState = voiceToTextParser.voiceState.collectAsState()

    var userTextField by rememberSaveable {
        mutableStateOf("")
    }

    val chatViewModel: ChatViewModel = viewModel { ChatViewModel() }
    val chatState = chatViewModel.chatState
    val isActiveJob = chatViewModel.isActiveJob.collectAsState(false)

    val listState = rememberLazyListState()
    var lastSpokenText by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(
        !voiceState.value.isSpeaking && voiceState.value.spokenText.isNotEmpty() && voiceState.value.spokenText != lastSpokenText
    ) {
        userTextField = voiceState.value.spokenText
        lastSpokenText = voiceState.value.spokenText
    }

    if (windowInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
        ChatScreenDesktop(
            voiceToTextParser = voiceToTextParser,
            userTextField = userTextField,
            chatState = chatState,
            listState = listState,
            voiceState = voiceState,
            isActiveJob = isActiveJob,
            onMessageSent = { message->
                chatViewModel.onEvent(ChatScreenEvents.MessageSent(message = message, username = login, password = password))
                userTextField = ""
            },
            onChatTextField = {
                userTextField = it
            }
        )
    } else {
        ChatScreenMobile(
            voiceToTextParser = voiceToTextParser,
            userTextField = userTextField,
            chatState = chatState,
            listState = listState,
            voiceState = voiceState,
            isActiveJob = isActiveJob,
            onMessageSent = { message->
                chatViewModel.onEvent(ChatScreenEvents.MessageSent(message = message, username = login, password = password))
                userTextField = ""
            },
            onChatTextField = {
                userTextField = it
            }
        )
    }
}
