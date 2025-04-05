package org.ailingo.app.features.chat.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.message
import ailingo.composeapp.generated.resources.send_message
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.core.presentation.snackbar.SnackbarController
import org.ailingo.app.core.presentation.snackbar.SnackbarEvent
import org.ailingo.app.core.utils.deviceinfo.util.PlatformName
import org.ailingo.app.core.utils.voice.VoiceToTextState
import org.ailingo.app.core.utils.voice.rememberVoiceToTextHandler
import org.ailingo.app.features.chat.data.model.Conversation
import org.ailingo.app.features.chat.presentation.model.MessageType
import org.ailingo.app.getPlatformName
import org.ailingo.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatScreen(
    topicName: String,
    topicImage: String,
    chatUiState: UiState<MutableList<Conversation>>,
    messagesState: List<Conversation>,
    onEvent: (ChatEvents) -> Unit,
) {
    var messageInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val voiceToTextHandler = rememberVoiceToTextHandler()
    val voiceState by voiceToTextHandler.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(voiceState) {
        if (voiceState is VoiceToTextState.Result) {
            val resultText = (voiceState as VoiceToTextState.Result).text
            if (resultText.isNotBlank()) {
                messageInput = resultText
            }
        } else if (voiceState is VoiceToTextState.Error) {
            val errorMessage = (voiceState as VoiceToTextState.Error).message
            Logger.i("Voice Recognition Error: $errorMessage")
            SnackbarController.sendEvent(SnackbarEvent(message = errorMessage))
        }
    }

    LaunchedEffect(messagesState) {
        if (messagesState.isNotEmpty()) {
            listState.scrollToItem(messagesState.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, bottom = 8.dp)) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            shape = CircleShape
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(4.dp)
            ) {
                Card(
                    shape = CircleShape,
                    modifier = Modifier.size(50.dp)
                ) {
                    AsyncImage(
                        model = topicImage,
                        contentDescription = "Topic name",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    topicName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = listState
            ) {
                items(messagesState) { message ->
                    ChatMessageItem(message = message)
                }
                when (chatUiState) {
                    is UiState.Error -> {
                        item {
                            ChatMessageItem(message = Conversation(id = "", conversationId = "", content = chatUiState.message, timestamp = "", type = MessageType.BOT.name))
                        }
                    }
                    is UiState.Idle -> {}
                    is UiState.Loading -> {
                        item {
                            ChatMessageItem(message = Conversation(id = "", conversationId = "", content = "Waiting for response...", timestamp = "", type = MessageType.BOT.name))
                        }
                    }
                    is UiState.Success -> {}
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(stringResource(Res.string.message)) },
                    shape = RoundedCornerShape(32.dp),
                    maxLines = 3,
                    trailingIcon = {
                        if (getPlatformName() != PlatformName.Web) {
                            IconButton(onClick = {
                                if (voiceToTextHandler.isAvailable) {
                                    coroutineScope.launch {
                                        if (voiceState is VoiceToTextState.Listening) {
                                            voiceToTextHandler.stopListening()
                                        } else {
                                            messageInput = ""
                                            voiceToTextHandler.startListening("en-US")
                                        }
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Filled.Mic, contentDescription = "mic")
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = {
                        if (messageInput.isNotBlank()) {
                            onEvent(ChatEvents.OnSendMessage(messageInput))
                            messageInput = ""
                        }
                    },
                    enabled = chatUiState !is UiState.Loading,
                    modifier = Modifier.height(OutlinedTextFieldDefaults.MinHeight),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Text(stringResource(Res.string.send_message))
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: Conversation) {

    val alignment = if (message.type == MessageType.USER.name) Alignment.End else Alignment.Start
    val backgroundColor = if (message.type == MessageType.USER.name) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = backgroundColor,
            modifier = Modifier.padding(
                PaddingValues(
                    start = if (message.type == MessageType.USER.name) 52.dp else 0.dp,
                    top = 0.dp,
                    end = if (message.type == MessageType.USER.name) 0.dp else 52.dp,
                    bottom = 4.dp
                )
            )
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(8.dp)
            )
        }
        val timestampString = message.timestamp
        var displayTimestamp: String
        if (timestampString.contains("T") && timestampString.length > timestampString.indexOf("T") + 5) {
            val datePart = timestampString.substringBefore('T')
            val timePart = timestampString.substringAfter('T').substring(0, 5)
            displayTimestamp = "$datePart $timePart".trim()
        } else {
            displayTimestamp = ""
        }
        Text(
            text = displayTimestamp,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(
                start = if (message.type == MessageType.USER.name) 0.dp else 8.dp,
                end = if (message.type == MessageType.USER.name) 8.dp else 0.dp,
                bottom = 8.dp
            )
        )
    }
}

@Composable
@Preview
fun PreviewChatScreen() {
    AppTheme {
        val previewMessages = mutableListOf(
            Conversation("1", "conv1", "Hello!", "2024-07-20T10:00:00Z", "USER"),
            Conversation(
                "2",
                "conv1",
                "Hi there! How can I help you today?",
                "2024-07-20T10:00:30Z",
                "BOT"
            ),
            Conversation(
                "3",
                "conv1",
                "I have a question about...",
                "2024-07-20T10:01:00Z",
                "USER"
            ),
            Conversation(
                "4",
                "conv1",
                "Okay, I'm listening. Please tell me your question.",
                "2024-07-20T10:01:30Z",
                "BOT"
            ),
            Conversation("5", "conv1", "It's about...", "2024-07-20T10:02:00Z", "USER"),
            Conversation("6", "conv1", "Let me see...", "2024-07-20T10:02:30Z", "BOT"),
            Conversation("7", "conv1", "And another question...", "2024-07-20T10:03:00Z", "USER")
        )
        ChatScreen(
            topicName = "Name",
            topicImage = "",
            chatUiState = UiState.Success(previewMessages),
            messagesState = previewMessages,
            onEvent = { }
        )
    }
}