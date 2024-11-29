package org.ailingo.app.features.registration.presentation.uploadavatar

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.back
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ailingo.app.core.utils.presentation.ErrorScreen
import org.ailingo.app.core.utils.presentation.LoadingScreen
import org.jetbrains.compose.resources.stringResource

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
    when (val state = registerState.value) {
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
            ErrorScreen(
                errorMessage = state.message,
                onButtonClick = {
                    uploadAvatarViewModel.onEvent(UploadAvatarEvent.OnBackToEmptyUploadAvatar)
                },
                buttonMessage = stringResource(Res.string.back)
            )
        }

        RegisterApiState.Loading -> {
            LoadingScreen()
        }

        is RegisterApiState.Success -> {
            onNavigateToChatScreen()
        }
    }
}
