package org.ailingo.app.features.login.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.login
import ailingo.composeapp.generated.resources.login_cannot_be_empty
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginTextField(
    textValue: String,
    onValueChange: (String) -> Unit,
    onNext: (KeyboardActionScope.() -> Unit),
    showErrorText: Boolean
) {
    Column {
        OutlinedTextField(
            value = textValue,
            onValueChange = {
                onValueChange(it)
            },
            label = { Text(text = stringResource(Res.string.login)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = onNext
            ),
            isError = showErrorText,
        )

        if (showErrorText) {
            Text(
                text = stringResource(Res.string.login_cannot_be_empty),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}
