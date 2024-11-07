package org.ailingo.app.features.login.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.welcome_back
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginWelcomeMessage() {
    Text(
        text = stringResource(Res.string.welcome_back),
        style = MaterialTheme.typography.headlineLarge
    )
    Spacer(modifier = Modifier.height(8.dp))
}
