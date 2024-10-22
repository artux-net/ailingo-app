package org.ailingo.app.feature_login.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable


@Composable
fun LoginPasswordVisibilityIcon(passwordVisible: Boolean, onPasswordVisibleChange: () -> Unit) {
    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
    IconButton(onClick = onPasswordVisibleChange) {
        Icon(icon, contentDescription = null)
    }
}