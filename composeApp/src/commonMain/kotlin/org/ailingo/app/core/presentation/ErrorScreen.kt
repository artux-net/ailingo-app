package org.ailingo.app.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(
    errorMessage: String? = null,
    buttonMessage: String? = null,
    onButtonClick: (() -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
    contentAlignment: Alignment = Alignment.TopCenter,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = contentAlignment) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            modifier = modifier
                .padding(8.dp)
        ) {
            if (errorMessage != null) {
                Text(
                    errorMessage,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    style = textStyle
                )
            }
            if (buttonMessage != null && onButtonClick != null) {
                Button(
                    onClick = onButtonClick,
                    modifier = Modifier
                        .width(OutlinedTextFieldDefaults.MinWidth)
                        .defaultMinSize(minHeight = OutlinedTextFieldDefaults.MinHeight)
                ) {
                    Text(buttonMessage)
                }
            }
            if (content != null) content()
        }
    }
}