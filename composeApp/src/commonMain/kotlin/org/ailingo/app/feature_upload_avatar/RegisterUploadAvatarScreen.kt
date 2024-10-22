package org.ailingo.app.feature_upload_avatar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.ailingo.app.feature_register.presentation.RegisterError
import org.ailingo.app.feature_register.presentation.RegisterLoading
import org.ailingo.app.feature_register.presentation.RegisterUiState

@Composable
fun UploadAvatarScreen(
    component: UploadAvatarComponent,
    login: String,
    password: String,
    email: String,
    name: String,
    onNavigateToRegisterScreen: () -> Unit
) {
    val registerState = component.registerState.collectAsState()
    when (registerState.value) {
        RegisterUiState.Empty -> {
            RegisterUploadAvatarEmpty(
                component,
                login,
                password,
                email,
                name,
                onNavigateToRegisterScreen
            )
        }

        is RegisterUiState.Error -> {
            RegisterError(
                errorMessage = (registerState.value as RegisterUiState.Error).message,
                onBackToEmptyState = {
                    component.onEvent(UploadAvatarEvent.OnBackToEmptyUploadAvatar)
                    component.onEvent(UploadAvatarEvent.OnNavigateToRegisterScreen)
                }
            )
        }

        RegisterUiState.Loading -> {
            RegisterLoading()
        }

        is RegisterUiState.Success -> {
            component.onEvent(
                UploadAvatarEvent.OnNavigateToChatScreen
            )
        }
    }
}