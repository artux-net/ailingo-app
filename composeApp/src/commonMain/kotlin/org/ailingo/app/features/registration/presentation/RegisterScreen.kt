package org.ailingo.app.features.registration.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.already_have_account
import ailingo.composeapp.generated.resources.continue_app
import ailingo.composeapp.generated.resources.email
import ailingo.composeapp.generated.resources.email_invalid
import ailingo.composeapp.generated.resources.enter_password
import ailingo.composeapp.generated.resources.enter_your_email
import ailingo.composeapp.generated.resources.enter_your_login
import ailingo.composeapp.generated.resources.enter_your_name
import ailingo.composeapp.generated.resources.log_in
import ailingo.composeapp.generated.resources.login
import ailingo.composeapp.generated.resources.login_invalid
import ailingo.composeapp.generated.resources.name_invalid
import ailingo.composeapp.generated.resources.password
import ailingo.composeapp.generated.resources.password_invalid
import ailingo.composeapp.generated.resources.register_subtitle
import ailingo.composeapp.generated.resources.register_title
import ailingo.composeapp.generated.resources.username
import ailingo.composeapp.generated.resources.visibility
import ailingo.composeapp.generated.resources.visibility_off
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.SmallLoadingIndicator
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.core.presentation.snackbar.SnackbarController
import org.ailingo.app.core.presentation.snackbar.SnackbarEvent
import org.ailingo.app.features.registration.data.model.RegistrationRequest
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegistrationScreen(
    onNavigateToLoginPage: () -> Unit,
    onNavigateToVerifyEmail: (email: String, password: String) -> Unit,
    pendingRegistrationState: UiState<Unit>,
    onEvent: (RegistrationEvent) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isLoginValid by remember { mutableStateOf(true) }
    var isNameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }

    RegistrationContent(
        login = login,
        name = name,
        email = email,
        password = password,
        passwordVisible = passwordVisible,
        isLoginValid = isLoginValid,
        isNameValid = isNameValid,
        isEmailValid = isEmailValid,
        isPasswordValid = isPasswordValid,
        onLoginChange = { newLogin ->
            login = newLogin.trim()
        },
        onNameChange = { newName ->
            name = newName.trim()
        },
        onEmailChange = { newEmail ->
            email = newEmail.trim()
        },
        onPasswordChange = { newPassword ->
            password = newPassword.trim()
        },
        onPasswordVisibleChange = { newPasswordVisible ->
            passwordVisible = newPasswordVisible
        },
        onNavigateToLoginPage = onNavigateToLoginPage,
        onRegisterClick = {
            isNameValid = name.length in 2..24 && name.isNotBlank()
            isEmailValid = isValidEmail(email)
            isPasswordValid = password.length in 8..24 && password.isNotBlank()
            isLoginValid = login.length in 4..16 && password.isNotBlank()

            if (isNameValid && isEmailValid && isPasswordValid && isLoginValid) {
                onEvent(RegistrationEvent.OnRegisterUser(RegistrationRequest(login, password, email, name)))
            }
        },
        pendingRegistrationState = pendingRegistrationState
    )

    LaunchedEffect(key1 = pendingRegistrationState) {
        if (pendingRegistrationState is UiState.Success) {
            onNavigateToVerifyEmail(email, password)
            onEvent(RegistrationEvent.OnBackToEmptyState)
        }
    }
}

@Composable
fun RegistrationContent(
    login: String,
    name: String,
    email: String,
    password: String,
    passwordVisible: Boolean,
    isLoginValid: Boolean,
    isNameValid: Boolean,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    onLoginChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibleChange: (Boolean) -> Unit,
    onNavigateToLoginPage: () -> Unit,
    onRegisterClick: () -> Unit,
    pendingRegistrationState: UiState<Unit>
) {
    val focusRequesterLogin = remember { FocusRequester() }
    val focusRequesterName = remember { FocusRequester() }
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(pendingRegistrationState) {
        if (pendingRegistrationState is UiState.Error) {
            scope.launch {
                SnackbarController.sendEvent(
                    event = SnackbarEvent(
                        message = pendingRegistrationState.message
                    )
                )
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                stringResource(Res.string.register_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            VerticalSpacer(4.dp)
            Text(
                stringResource(Res.string.register_subtitle),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
            VerticalSpacer(8.dp)

            InputTextField(
                labelResId = Res.string.login,
                placeholderResId = Res.string.enter_your_login,
                value = login,
                onValueChange = onLoginChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequesterName.requestFocus() }),
                focusRequester = focusRequesterLogin,
                isError = !isLoginValid,
                errorMessage = if (!isLoginValid) stringResource(Res.string.login_invalid) else null
            )
            VerticalSpacer(8.dp)
            InputTextField(
                labelResId = Res.string.username,
                placeholderResId = Res.string.enter_your_name,
                value = name,
                onValueChange = onNameChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequesterEmail.requestFocus() }),
                focusRequester = focusRequesterName,
                isError = !isNameValid,
                errorMessage = if (!isNameValid) stringResource(Res.string.name_invalid) else null
            )
            VerticalSpacer(8.dp)

            InputTextField(
                labelResId = Res.string.email,
                placeholderResId = Res.string.enter_your_email,
                value = email,
                onValueChange = onEmailChange,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequesterPassword.requestFocus() }),
                focusRequester = focusRequesterEmail,
                isError = !isEmailValid,
                errorMessage = if (!isEmailValid) stringResource(Res.string.email_invalid) else null
            )
            VerticalSpacer(8.dp)

            InputTextField(
                labelResId = Res.string.password,
                placeholderResId = Res.string.enter_password,
                value = password,
                onValueChange = onPasswordChange,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (isNameValid && isEmailValid && isPasswordValid && isLoginValid) {
                        onRegisterClick()
                    }
                }),
                focusRequester = focusRequesterPassword,
                trailingIcon = {
                    val image =
                        if (passwordVisible) Res.drawable.visibility else Res.drawable.visibility_off
                    IconButton(onClick = {
                        onPasswordVisibleChange(!passwordVisible)
                    }) {
                        Icon(painter = painterResource(image), contentDescription = null)
                    }
                },
                isError = !isPasswordValid,
                errorMessage = if (!isPasswordValid) stringResource(Res.string.password_invalid) else null
            )
            VerticalSpacer(8.dp)
            Button(
                onClick = {
                    onRegisterClick()
                },
                modifier = Modifier
                    .width(OutlinedTextFieldDefaults.MinWidth)
                    .defaultMinSize(minHeight = OutlinedTextFieldDefaults.MinHeight),
                enabled = pendingRegistrationState !is UiState.Loading,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    stringResource(Res.string.continue_app),
                    style = MaterialTheme.typography.titleLarge
                )
                if (pendingRegistrationState is UiState.Loading) {
                    Spacer(modifier = Modifier.width(16.dp))
                    SmallLoadingIndicator()
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    stringResource(Res.string.already_have_account)
                )
                Text(
                    stringResource(Res.string.log_in),
                    modifier = Modifier.clickable {
                        onNavigateToLoginPage()
                    },
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun VerticalSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun InputTextField(
    labelResId: StringResource,
    placeholderResId: StringResource,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    focusRequester: FocusRequester,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Text(stringResource(labelResId), style = MaterialTheme.typography.titleMedium)
    VerticalSpacer(4.dp)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .width(OutlinedTextFieldDefaults.MinWidth)
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(16.dp),
        placeholder = {
            Text(stringResource(placeholderResId), color = Color.Gray)
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        isError = isError
    )
    if (isError && errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)(@{1})(.{1,})(\\.)(.{1,})"
    return email.matches(emailRegex.toRegex())
}