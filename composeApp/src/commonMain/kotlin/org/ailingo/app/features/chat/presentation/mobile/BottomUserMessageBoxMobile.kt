package org.ailingo.app.features.chat.presentation.mobile

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.message
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Mic
import compose.icons.feathericons.MicOff
import compose.icons.feathericons.Send
import kotlinx.coroutines.launch
import org.ailingo.app.core.utils.voice.VoiceStates
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.features.chat.data.model.Message
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomUserMessageBoxMobile(
    userTextField: String,
    voiceToTextParser: VoiceToTextParser,
    voiceState: State<VoiceStates>,
    messages: List<Message>,
    listState: LazyListState,
    isActiveJob: Boolean,
    onMessageSent: (String) -> Unit,
    onChatTextField: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.padding(8.dp),
    ) {
        OutlinedTextField(
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            value = userTextField,
            onValueChange = {
                onChatTextField(it)
            },
            label = {
                Text(stringResource(Res.string.message))
            },
            trailingIcon = {
                Row {
                    IconButton(onClick = {
                        if (voiceState.value.isSpeaking) {
                            voiceToTextParser.stopListening()
                        } else {
                            voiceToTextParser.startListening()
                        }
                    }) {
                        Icon(
                            imageVector = if (voiceState.value.isSpeaking) FeatherIcons.Mic else FeatherIcons.MicOff,
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    if (!isActiveJob) {
                        IconButton(onClick = {
                            if (userTextField.isNotEmpty()) {
                                onMessageSent(userTextField)
                                scope.launch {
                                    listState.scrollToItem(messages.size - 1)
                                }
                            }
                        }) {
                            Icon(imageVector = FeatherIcons.Send, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        )
    }
}
