package org.ailingo.app.feature_login.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.forgot_password
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginForgotPasswordLink(onClick: () -> Unit) {
    Box(
        modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            modifier = Modifier.clickable { onClick() },
            text = stringResource(Res.string.forgot_password),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.End
        )
    }
}