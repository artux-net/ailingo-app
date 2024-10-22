package org.ailingo.app.feature_reset_password.presentation

sealed class ResetPasswordEvent {
    data object OnNavigateGetStartedScreen: ResetPasswordEvent()
}