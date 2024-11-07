package org.ailingo.app.features.introduction.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.get_started
import ailingo.composeapp.generated.resources.log_in
import ailingo.composeapp.generated.resources.sign_up
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource

@Composable
fun GetStartedScreen(
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToRegisterScreen: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            stringResource(Res.string.get_started),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(shape = MaterialTheme.shapes.small, onClick = {
                onNavigateToLoginScreen()
            }) {
                Text(stringResource(Res.string.log_in))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(shape = MaterialTheme.shapes.small, onClick = {
                onNavigateToRegisterScreen()
            }) {
                Text(stringResource(Res.string.sign_up))
            }
        }
    }
}
