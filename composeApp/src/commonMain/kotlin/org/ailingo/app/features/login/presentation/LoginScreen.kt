package org.ailingo.app.features.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    onNavigateToChatScreen: () -> Unit,
    onNavigateToRegisterScreen: () -> Unit,
    onNavigateToResetPasswordScreen: () -> Unit,
    loginViewModel: LoginViewModel
) {
    val loginState = loginViewModel.loginState.collectAsStateWithLifecycle().value

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val isLoading = rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(isLoading.value) {
        if (isLoading.value) {
            delay(500L) // just for cute ui
            isLoading.value = false
        }
    }

    when (loginState) {
        LoginUiState.Empty -> {
            LoginMainScreen(
                onLoginUser = {
                    loginViewModel.onEvent(
                        LoginScreenEvent.OnLoginUser(
                            loginViewModel.login,
                            loginViewModel.password
                        )
                    )
                },
                login = loginViewModel.login,
                onLoginChange = {
                    loginViewModel.login = it
                },
                password = loginViewModel.password,
                onPasswordChange = {
                    loginViewModel.password = it
                },
                passwordVisible,
                onPasswordVisibleChange = {
                    passwordVisible = !passwordVisible
                },
                isLoading,
                onNavigateToRegisterScreen = onNavigateToRegisterScreen,
                onNavigateToResetPasswordScreen = onNavigateToResetPasswordScreen
            )
        }

        is LoginUiState.Error -> {
            LoginErrorScreen(
                onEmptyState = {
                    loginViewModel.onEvent(LoginScreenEvent.OnBackToEmptyState)
                },
                errorMessage = loginState.message
            )
        }

        LoginUiState.Loading -> {
            LoginLoadingScreen()
        }

        is LoginUiState.Success -> {
            onNavigateToChatScreen()
        }
    }
}
