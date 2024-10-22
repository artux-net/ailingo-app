package org.ailingo.app.feature_login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.delay


@Composable
fun LoginScreen(
    component: LoginScreenComponent
) {
    val loginState = component.loginState.collectAsState()
    var login by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("admin"))
    }
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("pass"))
    }
    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val isLoading = rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(isLoading.value) {
        if (isLoading.value) {
            delay(500L) //just for cute ui
            isLoading.value = false
        }
    }

    when (loginState.value) {
        LoginUiState.Empty -> {
            LoginMainScreen(
                onLoginUser = {
                    component.onEvent(LoginScreenEvent.OnLoginUser(login.text, password.text))
                },
                login = login,
                onLoginChange = {
                    login = it
                },
                password = password,
                onPasswordChange = {
                    password = it
                },
                passwordVisible,
                onPasswordVisibleChange = {
                    passwordVisible = !passwordVisible
                },
                isLoading,
                component
            )
        }

        is LoginUiState.Error -> {
            LoginErrorScreen(component, (loginState.value as LoginUiState.Error).message)
        }

        LoginUiState.Loading -> {
            LoginLoadingScreen()
        }

        is LoginUiState.Success -> {
            component.onEvent(
                LoginScreenEvent.OnNavigateToChatScreen
            )
        }
    }
}