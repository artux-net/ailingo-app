package org.ailingo.app.feature_register.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import dev.icerock.moko.resources.compose.stringResource
import org.ailingo.app.SharedRes
import org.ailingo.app.core.util.VoiceToTextParser
import org.ailingo.app.feature_login.presentation.LoginScreen
import org.ailingo.app.feature_register.data.model.UserRegistrationData

@Composable
fun RegisterStart(
    registerViewModel: RegistrationViewModel,
    navigator: Navigator,
    voiceToTextParser: VoiceToTextParser,
    login: MutableState<String>,
    password: MutableState<String>,
    email: MutableState<String>,
    name: MutableState<String>,
    avatar: MutableState<String>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        val passwordFieldFocusRequester = rememberUpdatedState(FocusRequester())
        val emailFieldFocusRequester = rememberUpdatedState(FocusRequester())
        val nameFieldFocusRequester = rememberUpdatedState(FocusRequester())
        val avatarFieldFocusRequester = rememberUpdatedState(FocusRequester())
        Text(
            stringResource(SharedRes.strings.create_your_account),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = login.value,
            onValueChange = {
                login.value = it
            },
            label = { Text(text = stringResource(SharedRes.strings.login)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    passwordFieldFocusRequester.value.requestFocus()
                }
            ),
        )

        OutlinedTextField(
            value = password.value,
            onValueChange = {
                password.value = it
            },
            label = { Text(text = stringResource(SharedRes.strings.password)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    emailFieldFocusRequester.value.requestFocus()
                }
            ),
            modifier = Modifier.focusRequester(passwordFieldFocusRequester.value)
        )

        OutlinedTextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            label = { Text(text = stringResource(SharedRes.strings.email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    nameFieldFocusRequester.value.requestFocus()
                }
            ),
            modifier = Modifier.focusRequester(emailFieldFocusRequester.value)
        )

        OutlinedTextField(
            value = name.value,
            onValueChange = {
                name.value = it
            },
            label = { Text(text = stringResource(SharedRes.strings.name)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    avatarFieldFocusRequester.value.requestFocus()
                }
            ),
            modifier = Modifier.focusRequester(nameFieldFocusRequester.value)
        )


        OutlinedTextField(
            value = avatar.value,
            onValueChange = {
                avatar.value = it
            },
            label = { Text(text = stringResource(SharedRes.strings.avatar)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    registerViewModel.registerUser(
                        UserRegistrationData(
                            login.value, password.value, email.value, name.value, avatar.value
                        )
                    )
                }
            ),
            modifier = Modifier.focusRequester(avatarFieldFocusRequester.value)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .width(OutlinedTextFieldDefaults.MinWidth)
                .height(OutlinedTextFieldDefaults.MinHeight),
            shape = MaterialTheme.shapes.small,
            onClick = {
                registerViewModel.registerUser(
                    UserRegistrationData(
                        login.value, password.value, email.value, name.value, avatar.value
                    )
                )
            }
        ) {
            Text(stringResource(SharedRes.strings.continue_app))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(
                stringResource(SharedRes.strings.already_have_an_account)
            )
            Text(" ")
            Text(
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(SharedRes.strings.log_in),
                modifier = Modifier.clickable {
                    navigator.push(LoginScreen(voiceToTextParser))
                }
            )
        }
    }
}