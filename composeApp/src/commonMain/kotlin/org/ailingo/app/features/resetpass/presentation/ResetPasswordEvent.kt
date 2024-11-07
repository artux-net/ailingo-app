package org.ailingo.app.features.resetpass.presentation

sealed class ResetPasswordEvent {
    data object OnNavigateGetStartedScreen : ResetPasswordEvent()
}
