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
import org.ailingo.app.core.helper.voice.VoiceToTextParser
import org.ailingo.app.core.helper.window.info.WindowInfo
import org.ailingo.app.core.helper.window.info.rememberWindowInfo
import org.ailingo.app.features.chat.presentation.desktop.ChatScreenDesktop

@Composable
fun ChatScreen(
    voiceToTextParser: VoiceToTextParser
) {
    val voiceState = voiceToTextParser.voiceState.collectAsState()

    var userTextField by rememberSaveable {
        mutableStateOf("")
    }

    val chatScreenViewModel: ChatScreenViewModel = viewModel { ChatScreenViewModel() }
    val chatState = chatScreenViewModel.chatState
    val isActiveJob = chatScreenViewModel.isActiveJob.collectAsState(false)

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

    val screenInfo = rememberWindowInfo()

    if (screenInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
        ChatScreenDesktop(
            voiceToTextParser = voiceToTextParser,
            chatTextField = userTextField,
            chatState = chatState,
            listState = listState,
            voiceState = voiceState,
            onMessageSent = {
                chatScreenViewModel.onEvent(ChatScreenEvents.MessageSent(it))
                userTextField = ""
            },
            isActiveJob = isActiveJob,
            onChatTextField = {
                userTextField = it
            }
        )
    } else {
        // TODO FOR MOBILE
//        ChatScreenMobile(
//            voiceToTextParser,
//            chatTextField,
//            chatState,
//            listState,
//            voiceState,
//            component,
//            isActiveJob,
//            onChatTextField = {
//                chatTextField = it
//            }
//        )
    }
}
