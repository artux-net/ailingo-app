package org.ailingo.app.features.registration.presentation.uploadavatar

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ailingo.app.features.registration.presentation.RegisterError
import org.ailingo.app.features.registration.presentation.RegisterLoading

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
        RegisterApiState.Empty -> {
            RegisterUploadAvatarEmpty(
                login = login,
                password = password,
                email = email,
                name = name,
                onNavigateToRegisterScreen = onNavigateToRegisterScreen,
                uploadAvatarViewModel = uploadAvatarViewModel
            )
        }

        is RegisterApiState.Error -> {
            RegisterError(
                errorMessage = (registerState.value as RegisterApiState.Error).message,
                onBackToEmptyState = {
                    uploadAvatarViewModel.onEvent(UploadAvatarEvent.OnBackToEmptyUploadAvatar)
                    onNavigateToRegisterScreen()
                }
            )
        }

        RegisterApiState.Loading -> {
            RegisterLoading()
        }

        is RegisterApiState.Success -> {
            onNavigateToChatScreen()
        }
    }
}
