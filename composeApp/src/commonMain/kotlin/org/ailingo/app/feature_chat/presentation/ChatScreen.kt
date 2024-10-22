package org.ailingo.app.feature_chat.presentation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import org.ailingo.app.core.helper_voice.VoiceToTextParser
import org.ailingo.app.core.helper_window_info.WindowInfo
import org.ailingo.app.core.helper_window_info.rememberWindowInfo
import org.ailingo.app.feature_chat.presentation.desktop.ChatScreenDesktop
import org.ailingo.app.feature_chat.presentation.mobile.ChatScreenMobile

@Composable
fun ChatScreen(
    voiceToTextParser: VoiceToTextParser,
    component: ChatScreenComponent
) {
    val voiceState = voiceToTextParser.voiceState.collectAsState()

    var chatTextField by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val chatState = component.chatState
    val isActiveJob = component.isActiveJob.collectAsState(false)

    val listState = rememberLazyListState()
    var lastSpokenText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    LaunchedEffect(!voiceState.value.isSpeaking && voiceState.value.spokenText.text.isNotEmpty() && voiceState.value.spokenText != lastSpokenText) {
        chatTextField = voiceState.value.spokenText
        lastSpokenText = voiceState.value.spokenText
    }

    val screenInfo = rememberWindowInfo()

    if (screenInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
        ChatScreenDesktop(
            voiceToTextParser = voiceToTextParser,
            chatTextField = chatTextField,
            chatState = chatState,
            listState = listState,
            voiceState = voiceState,
            component = component,
            isActiveJob = isActiveJob,
            onChatTextField = {
                chatTextField = it
            }
        )
    } else {
        ChatScreenMobile(
            voiceToTextParser,
            chatTextField,
            chatState,
            listState,
            voiceState,
            component,
            isActiveJob,
            onChatTextField = {
                chatTextField = it
            }
        )
    }
}