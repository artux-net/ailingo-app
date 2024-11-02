package org.ailingo.app.feature_upload_avatar

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ailingo.app.feature_register.presentation.RegisterError
import org.ailingo.app.feature_register.presentation.RegisterLoading
import org.ailingo.app.feature_register.presentation.RegisterUiState

@Composable
fun UploadAvatarScreen(
    login: String,
    password: String,
    email: String,
    name: String,
    onNavigateToRegisterScreen: () -> Unit,
    onNavigateToChatScreen: () -> Unit,
    uploadAvatarViewModel: UploadAvatarViewModel
) {
    val registerState = uploadAvatarViewModel.registerState.collectAsStateWithLifecycle()
    when (registerState.value) {
        RegisterUiState.Empty -> {
            RegisterUploadAvatarEmpty(
                login = login,
                password = password,
                email = email,
                name = name,
                onNavigateToRegisterScreen = onNavigateToRegisterScreen,
                uploadAvatarViewModel = uploadAvatarViewModel
            )
        }

        is RegisterUiState.Error -> {
            RegisterError(
                errorMessage = (registerState.value as RegisterUiState.Error).message,
                onBackToEmptyState = {
                    uploadAvatarViewModel.onEvent(UploadAvatarEvent.OnBackToEmptyUploadAvatar)
                    onNavigateToRegisterScreen()
                }
            )
        }

        RegisterUiState.Loading -> {
            RegisterLoading()
        }

        is RegisterUiState.Success -> {
            onNavigateToChatScreen()
        }
    }
}