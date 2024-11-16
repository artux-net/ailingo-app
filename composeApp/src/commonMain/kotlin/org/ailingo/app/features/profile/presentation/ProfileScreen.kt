package org.ailingo.app.features.profile.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.exit
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onExit: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text("ProfileScreen")
            OutlinedButton(onClick = onExit) {
                Text(stringResource(Res.string.exit))
            }
        }
    }
}