package org.ailingo.app.feature_login.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_chat.presentation.ChatScreen
import org.ailingo.app.feature_register.presentation.RegistrationScreen
import org.ailingo.app.feature_reset_password.presentation.ResetPasswordScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginMainScreen(
    navigator: Navigator,
    loginViewModel: LoginViewModel,
    voiceToTextParser: VoiceToTextParser,
    login: String,
    onLoginChange:(String) -> Unit,
    password: String,
    onPasswordChange:(String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        val focusManager = LocalFocusManager.current
        val passwordFieldFocusRequester = rememberUpdatedState(FocusRequester())
        val keyboardController = LocalSoftwareKeyboardController.current
        Text(
            text = stringResource(SharedRes.strings.welcome_back),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = login,
            onValueChange = onLoginChange,
            label = { Text(text = stringResource(SharedRes.strings.login)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    passwordFieldFocusRequester.value.requestFocus()
                }
            ),
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(text = stringResource(SharedRes.strings.password)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    navigator.push(ChatScreen(voiceToTextParser))
                }
            ),
            modifier = Modifier.focusRequester(passwordFieldFocusRequester.value)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                modifier = Modifier.clickable {
                    navigator.push(ResetPasswordScreen(voiceToTextParser))
                },
                text = stringResource(SharedRes.strings.forgot_password),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .width(OutlinedTextFieldDefaults.MinWidth)
                .height(OutlinedTextFieldDefaults.MinHeight),
            shape = MaterialTheme.shapes.small,
            onClick = {
                loginViewModel.loginUser(login, password)
            }
        ) {
            Text(stringResource(SharedRes.strings.continue_app))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(
                stringResource(SharedRes.strings.dont_have_an_account)
            )
            Text(" ")
            Text(
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(SharedRes.strings.sign_up),
                modifier = Modifier.clickable {
                    navigator.push(RegistrationScreen(voiceToTextParser))
                }
            )

        }
    }
}