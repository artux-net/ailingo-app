package org.ailingo.app.feature_register.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.already_have_an_account
import ailingo.composeapp.generated.resources.create_your_account
import ailingo.composeapp.generated.resources.email
import ailingo.composeapp.generated.resources.log_in
import ailingo.composeapp.generated.resources.login
import ailingo.composeapp.generated.resources.name
import ailingo.composeapp.generated.resources.next
import ailingo.composeapp.generated.resources.password
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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterScreen(
    component: RegisterScreenComponent
) {
    val login = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val password = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val email = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val name = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val savedPhoto = rememberSaveable {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(500L) //just for cute ui
            isLoading = false
        }
    }
    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFieldFocusRequester = rememberUpdatedState(FocusRequester())
    val emailFieldFocusRequester = rememberUpdatedState(FocusRequester())
    val nameFieldFocusRequester = rememberUpdatedState(FocusRequester())
    var isLoginNotValid by remember {
        mutableStateOf(false)
    }
    var isPasswordNotValid by remember {
        mutableStateOf(false)
    }
    var isEmailNotValid by remember {
        mutableStateOf(false)
    }
    var isNameNotValid by remember {
        mutableStateOf(false)
    }
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
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
            Column {
                OutlinedTextField(
                    value = login.value,
                    onValueChange = {
                        login.value = it
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
                    isError = isLoginNotValid,
                    trailingIcon = {
                        if (isLoginNotValid) {
                            Icon(
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                if (isLoginNotValid) {
                    Text(
                        "Login must be between 4 and 16 characters",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                    )
                }
            }
            Column {
                OutlinedTextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
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
                    isError = isPasswordNotValid,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                if (isPasswordNotValid) {
                    Text(
                        "Password must be between 8 and 24 characters",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                    )
                }
            }
            Column {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
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
                    isError = isEmailNotValid,
                    trailingIcon = {
                        if (isEmailNotValid) {
                            Icon(
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                if (isEmailNotValid) {
                    Text(
                        "Invalid email format",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                    )
                }
            }
            Column {
                OutlinedTextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                    },
                    label = { Text(text = stringResource(Res.string.name)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            isLoading = true
                            isLoginNotValid =
                                login.value.text.length !in 4..16 || login.value.text.isBlank()
                            isPasswordNotValid =
                                password.value.text.length !in 8..24 || password.value.text.isBlank()
                            isEmailNotValid =
                                !isValidEmail(email.value.text) || email.value.text.isBlank()
                            isNameNotValid = name.value.text.isBlank()
                            if (!isLoginNotValid && !isPasswordNotValid && !isEmailNotValid && !isNameNotValid) {
                                component.onEvent(
                                    RegisterScreenEvent.OnNavigateToUploadAvatarScreen(
                                        login.value.text,
                                        password.value.text,
                                        email.value.text,
                                        name.value.text,
                                        savedPhoto.value
                                    )
                                )
                            }
                        }
                    ),
                    modifier = Modifier.focusRequester(nameFieldFocusRequester.value),
                    isError = isNameNotValid,
                    trailingIcon = {
                        if (isNameNotValid) {
                            Icon(
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                if (isNameNotValid) {
                    Text(
                        "Name cannot be blank",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp, start = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .width(OutlinedTextFieldDefaults.MinWidth)
                    .height(OutlinedTextFieldDefaults.MinHeight),
                shape = MaterialTheme.shapes.small,
                onClick = {
                    isLoading = true
                    isLoginNotValid =
                        login.value.text.length !in 4..16 || login.value.text.isBlank()
                    isPasswordNotValid =
                        password.value.text.length !in 8..24 || password.value.text.isBlank()
                    isEmailNotValid = !isValidEmail(email.value.text) || email.value.text.isBlank()
                    isNameNotValid = name.value.text.isBlank()
                    if (!isLoginNotValid && !isPasswordNotValid && !isEmailNotValid && !isNameNotValid) {
                        component.onEvent(
                            RegisterScreenEvent.OnNavigateToUploadAvatarScreen(
                                login.value.text,
                                password.value.text,
                                email.value.text,
                                name.value.text,
                                savedPhoto.value
                            )
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
                        component.onEvent(RegisterScreenEvent.OnNavigateToLoginScreen)
                    }
                )
            }
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return email.matches(emailRegex.toRegex())
}