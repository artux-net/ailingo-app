package org.ailingo.app.feature_register.presentation

import com.arkivanov.decompose.ComponentContext

class RegisterScreenComponent(
    componentContext: ComponentContext,
    private val onNavigateToUploadAvatarScreen: (login: String, password: String, email: String, name: String, savedPhoto: String) -> Unit,
    private val onNavigateToLoginScreen:() -> Unit
) : ComponentContext by componentContext {

    fun onEvent(event: RegisterScreenEvent) {
        when (event) {
            is RegisterScreenEvent.OnNavigateToUploadAvatarScreen -> {
                onNavigateToUploadAvatarScreen(
                    event.login,
                    event.password,
                    event.email,
                    event.name,
                    event.savedPhoto
                )
            }

            RegisterScreenEvent.OnNavigateToLoginScreen -> {
                onNavigateToLoginScreen()
            }
        }
    }
}