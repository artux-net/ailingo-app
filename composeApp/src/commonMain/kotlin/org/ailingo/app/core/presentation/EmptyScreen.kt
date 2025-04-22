package org.ailingo.app.core.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.here_is_empty
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyScreen(
    text: StringResource = Res.string.here_is_empty,
    image: DrawableResource? = null,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
        ) {
            Text(stringResource(text), style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)
            if (image != null) {
                Image(painter = painterResource(image), modifier = Modifier.size(150.dp), contentDescription = null)
            }
        }
    }
}