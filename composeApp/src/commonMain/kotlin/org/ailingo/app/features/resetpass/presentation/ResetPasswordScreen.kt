package org.ailingo.app.features.resetpass.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.back_to_the_selection
import ailingo.composeapp.generated.resources.continue_app
import ailingo.composeapp.generated.resources.email
import ailingo.composeapp.generated.resources.reset_your_password
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResetPasswordScreen(
    onNavigateToSelectScreen: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        Spacer(modifier = Modifier.weight(1f))
        Text(
            stringResource(Res.string.reset_your_password),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text(text = stringResource(Res.string.email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Focus on password field when 'Next' is clicked
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .width(OutlinedTextFieldDefaults.MinWidth)
                .height(OutlinedTextFieldDefaults.MinHeight),
            shape = MaterialTheme.shapes.small,
            onClick = {
            }
        ) {
            Text(stringResource(Res.string.continue_app))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            color = MaterialTheme.colorScheme.primary,
            text = stringResource(Res.string.back_to_the_selection),
            modifier = Modifier.clickable {
                onNavigateToSelectScreen()
            }
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}
