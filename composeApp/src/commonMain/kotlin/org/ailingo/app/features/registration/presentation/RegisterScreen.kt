package org.ailingo.app.features.registration.presentation

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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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
    val login = registerViewModel.login
    val password = registerViewModel.password
    val email = registerViewModel.email
    val name = registerViewModel.name
    var isLoading by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(500L) // just for cute ui
            isLoading = false
        }
    }
    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFieldFocusRequester = rememberUpdatedState(FocusRequester())
    val emailFieldFocusRequester = rememberUpdatedState(FocusRequester())
    val nameFieldFocusRequester = rememberUpdatedState(FocusRequester())
    var isLoginValid by remember {
        mutableStateOf(false)
    }
    var isPasswordValid by remember {
        mutableStateOf(false)
    }
    var isEmailValid by remember {
        mutableStateOf(false)
    }
    var isNameValid by remember {
        mutableStateOf(false)
    }

    fun onValidRegistrationForm(
        login: MutableState<String>,
        password: MutableState<String>,
        email: MutableState<String>,
        name: MutableState<String>,
        invoker: () -> Unit
    ) {
        isLoading = true
        isLoginValid = login.value.length in 4..16 && login.value.isNotEmpty()
        isPasswordValid = password.value.length in 8..24 && password.value.isNotEmpty()
        isEmailValid = isValidEmail(email.value) && email.value.isNotEmpty()
        isNameValid = name.value.isNotEmpty()

        if (isLoginValid && isPasswordValid && isEmailValid && isNameValid) {
            invoker()
        }
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
                    isError = isLoginValid,
                    trailingIcon = {
                        if (isLoginValid) {
                            Icon(
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                if (isLoginValid) {
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
                    isError = isPasswordValid,
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
                if (isPasswordValid) {
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
                    isError = isEmailValid,
                    trailingIcon = {
                        if (isEmailValid) {
                            Icon(
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                if (isEmailValid) {
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
                            onValidRegistrationForm(login, password, email, name) {
                                onNavigateToUploadAvatarScreen(
                                    login.value,
                                    password.value,
                                    email.value,
                                    name.value
                                )
                            }
                        }
                    ),
                    modifier = Modifier.focusRequester(nameFieldFocusRequester.value),
                    isError = isNameValid,
                    trailingIcon = {
                        if (isNameValid) {
                            Icon(
                                Icons.Filled.Error,
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                if (isNameValid) {
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
                    onValidRegistrationForm(
                        login,
                        password,
                        email,
                        name
                    ) {
                        onNavigateToUploadAvatarScreen(
                            login.value,
                            password.value,
                            email.value,
                            name.value
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

private fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return email.matches(emailRegex.toRegex())
}



