package org.ailingo.app.feature_login.presentation

sealed class LoginScreenEvent {
    data object OnNavigateToChatScreen: LoginScreenEvent()
    data object OnNavigateToResetPasswordScreen: LoginScreenEvent()
    data object OnNavigateToRegisterScreen: LoginScreenEvent()
    data class OnLoginUser(val login: String, val password: String): LoginScreenEvent()
    data object OnBackToEmptyState: LoginScreenEvent()
}