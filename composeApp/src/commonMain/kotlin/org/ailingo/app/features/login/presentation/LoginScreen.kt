package org.ailingo.app.features.login.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.dont_have_an_account
import ailingo.composeapp.generated.resources.email
import ailingo.composeapp.generated.resources.enter_password
import ailingo.composeapp.generated.resources.enter_your_email
import ailingo.composeapp.generated.resources.log_in
import ailingo.composeapp.generated.resources.login
import ailingo.composeapp.generated.resources.login_to_countinue
import ailingo.composeapp.generated.resources.password
import ailingo.composeapp.generated.resources.sign_up
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.SmallLoadingIndicator
import org.ailingo.app.core.presentation.custom.CustomTextField
import org.ailingo.app.core.presentation.snackbar.SnackbarController
import org.ailingo.app.core.presentation.snackbar.SnackbarEvent
import org.ailingo.app.core.utils.deviceinfo.util.PlatformName
import org.ailingo.app.getPlatformName
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    loginState: LoginUiState,
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToRegisterScreen: () -> Unit,
    onEvent: (LoginScreenEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterPassword = remember { FocusRequester() }

    LaunchedEffect(loginState) {
        if (loginState is LoginUiState.Error) {
            scope.launch {
                SnackbarController.sendEvent(
                    event = SnackbarEvent(
                        message = loginState.message
                    )
                )
            }
            onEvent(LoginScreenEvent.OnBackToEmptyState)
        }
        if (loginState is LoginUiState.Success) {
            onNavigateToHomeScreen()
        }
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Column {
            Text(
                stringResource(Res.string.login),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                stringResource(Res.string.login_to_countinue),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
            VerticalSpacer(16.dp)

            CustomTextField(
                labelResId = Res.string.email,
                placeholderResId = Res.string.enter_your_email,
                value = email.value,
                onValueChange = {
                    email.value = it
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequesterPassword.requestFocus() }),
                focusRequester = focusRequesterEmail
            )
            VerticalSpacer(16.dp)

            CustomTextField(
                labelResId = Res.string.password,
                placeholderResId = Res.string.enter_password,
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    onEvent(LoginScreenEvent.OnLoginUser(email.value, password.value))
                }),
                focusRequester = focusRequesterPassword,
                trailingIcon = {
                    val image =
                        if (passwordVisible.value) Res.drawable.visibility else Res.drawable.visibility_off
                    IconButton(onClick = {
                        passwordVisible.value = !passwordVisible.value
                    }) {
                        Icon(painter = painterResource(image), contentDescription = null)
                    }
                }
            )
            VerticalSpacer(16.dp)
            Button(
                onClick = {
                    onEvent(LoginScreenEvent.OnLoginUser(email.value, password.value))
                },
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .defaultMinSize(
                        minHeight = OutlinedTextFieldDefaults.MinHeight,
                    ).then(if (getPlatformName() == PlatformName.Android) Modifier.fillMaxWidth() else Modifier.defaultMinSize(minWidth = OutlinedTextFieldDefaults.MinWidth)),
                enabled = loginState !is LoginUiState.Loading
            ) {
                Text(
                    stringResource(Res.string.log_in),
                    style = MaterialTheme.typography.titleLarge
                )
                if (loginState is LoginUiState.Loading) {
                    Spacer(modifier = Modifier.width(16.dp))
                    SmallLoadingIndicator()
                }
            }
            VerticalSpacer(32.dp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                modifier = if (getPlatformName() == PlatformName.Android) Modifier.fillMaxWidth() else Modifier
            ) {
                Text(
                    stringResource(Res.string.dont_have_an_account)
                )
                Text(
                    stringResource(Res.string.sign_up),
                    modifier = Modifier.clickable {
                        onNavigateToRegisterScreen()
                    },
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            VerticalSpacer(32.dp)
        }
    }
}

@Composable
fun VerticalSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}