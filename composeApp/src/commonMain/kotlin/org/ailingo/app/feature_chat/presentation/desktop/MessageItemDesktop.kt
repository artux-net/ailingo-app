package org.ailingo.app.feature_chat.presentation.desktop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ailingo.app.feature_chat.data.model.Message
import org.ailingo.app.theme.WhiteBlueForBot
import org.ailingo.app.theme.WhiteBlueForUser


@Composable
fun MessageItemDesktop(message: Message) {
    val isSentByUser = message.isSentByUser
    val backgroundColor = if (isSentByUser) WhiteBlueForUser else WhiteBlueForBot

    Row {
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.weight(4f),
            horizontalArrangement = if (isSentByUser) Arrangement.Start else Arrangement.End
        ) {
            if (message.isSentByUser) {
                Spacer(modifier = Modifier.weight(1f))
            }
            Box(modifier = Modifier.weight(2f)) {
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .then(
                            if (isSentByUser) Modifier.align(Alignment.CenterEnd) else Modifier.align(
                                Alignment.CenterStart
                            )
                        ),
                    shape = MaterialTheme.shapes.large,
                    color = backgroundColor,
                    contentColor = Color.Black,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = message.text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
            if (!message.isSentByUser) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
