package org.ailingo.app.features.registration.presentation

sealed class RegisterScreenEvent {
    data class OnNavigateToUploadAvatarScreen(
        val login: String,
        val password: String,
        val email: String,
        val name: String,
        val savedPhoto: String
    ) : RegisterScreenEvent()

    data object OnNavigateToLoginScreen : RegisterScreenEvent()
}
