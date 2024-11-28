package org.ailingo.app.features.registration.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.already_have_an_account
import ailingo.composeapp.generated.resources.create_your_account
import ailingo.composeapp.generated.resources.email
import ailingo.composeapp.generated.resources.error
import ailingo.composeapp.generated.resources.invalid_email_format
import ailingo.composeapp.generated.resources.log_in
import ailingo.composeapp.generated.resources.login
import ailingo.composeapp.generated.resources.login_must_be_between
import ailingo.composeapp.generated.resources.name
import ailingo.composeapp.generated.resources.name_cannot_be_blank
import ailingo.composeapp.generated.resources.next
import ailingo.composeapp.generated.resources.password
import ailingo.composeapp.generated.resources.password_must_be_between
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterScreen(
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToUploadAvatarScreen: (
        login: String,
        password: String,
        email: String,
        name: String
    ) -> Unit,
    registerViewModel: RegisterViewModel
) {
    val uiState by registerViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = uiState.isLoading

    LaunchedEffect(Unit) {
        registerViewModel.onLoadingChange(true)
        delay(250) // Just for cute ui
        registerViewModel.onLoadingChange(false)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        RegistrationFormContent(
            onNavigateToLoginScreen,
            onNavigateToUploadAvatarScreen,
            registerViewModel,
            uiState
        )
    }
}

@Composable
fun RegistrationFormContent(
    onNavigateToLoginScreen: () -> Unit,
    onNavigateToUploadAvatarScreen: (login: String, password: String, email: String, name: String) -> Unit,
    registerViewModel: RegisterViewModel,
    uiState: RegisterUiState
) {
    val login = uiState.login
    val password = uiState.password
    val email = uiState.email
    val name = uiState.name

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val passwordFieldFocusRequester = rememberUpdatedState(FocusRequester())
    val emailFieldFocusRequester = rememberUpdatedState(FocusRequester())
    val nameFieldFocusRequester = rememberUpdatedState(FocusRequester())

    val isLoginValid = uiState.isLoginValid
    val isPasswordValid = uiState.isPasswordValid
    val isEmailValid = uiState.isEmailValid
    val isNameValid = uiState.isNameValid

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        Text(
            stringResource(Res.string.create_your_account),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column {
                OutlinedTextField(
                    value = login,
                    onValueChange = {
                        registerViewModel.onLoginChange(it)
                    },
                    label = { Text(text = stringResource(Res.string.login)) },
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
                    isError = !isLoginValid,
                    trailingIcon = {
                        if (!isLoginValid) {
                            Icon(
                                Icons.Filled.Error,
                                stringResource(Res.string.error),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                if (!isLoginValid) {
                    Text(
                        stringResource(Res.string.login_must_be_between),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        registerViewModel.onPasswordChange(it)
                    },
                    label = { Text(text = stringResource(Res.string.password)) },
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
                    isError = !isPasswordValid,
                    trailingIcon = {
                        val icon =
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            Icon(icon, contentDescription = null, tint = Color.Black)
                        }
                    },
                    modifier = Modifier.focusRequester(passwordFieldFocusRequester.value),
                )
                if (!isPasswordValid) {
                    Text(
                        stringResource(Res.string.password_must_be_between),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        registerViewModel.onEmailChange(it)
                    },
                    label = { Text(text = stringResource(Res.string.email)) },
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
                    modifier = Modifier.focusRequester(emailFieldFocusRequester.value),
                    isError = !isEmailValid,
                    trailingIcon = {
                        if (!isEmailValid) {
                            Icon(
                                Icons.Filled.Error,
                                stringResource(Res.string.error),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                if (!isEmailValid) {
                    Text(
                        stringResource(Res.string.invalid_email_format),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        registerViewModel.onNameChange(it)
                    },
                    label = { Text(text = stringResource(Res.string.name)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            registerViewModel.register {
                                onNavigateToUploadAvatarScreen(
                                    uiState.login,
                                    uiState.password,
                                    uiState.email,
                                    uiState.name
                                )
                            }
                        }
                    ),
                    modifier = Modifier.focusRequester(nameFieldFocusRequester.value),
                    isError = !isNameValid,
                    trailingIcon = {
                        if (!isNameValid) {
                            Icon(
                                Icons.Filled.Error,
                                stringResource(Res.string.error),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                if (!isNameValid) {
                    Text(
                        stringResource(Res.string.name_cannot_be_blank),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier
                        .width(OutlinedTextFieldDefaults.MinWidth)
                        .height(OutlinedTextFieldDefaults.MinHeight),
                    shape = MaterialTheme.shapes.small,
                    onClick = {
                        registerViewModel.register {
                            onNavigateToUploadAvatarScreen(
                                uiState.login,
                                uiState.password,
                                uiState.email,
                                uiState.name
                            )
                        }
                    }
                ) {
                    Text(stringResource(Res.string.next))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text(
                        stringResource(Res.string.already_have_an_account)
                    )
                    Text(" ")
                    Text(
                        color = MaterialTheme.colorScheme.primary,
                        text = stringResource(Res.string.log_in),
                        modifier = Modifier.clickable {
                            onNavigateToLoginScreen()
                        }
                    )
                }
            }
        }
    }
}