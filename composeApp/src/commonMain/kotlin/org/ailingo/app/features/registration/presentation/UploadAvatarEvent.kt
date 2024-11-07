package org.ailingo.app.features.registration.presentation

import org.ailingo.app.features.registration.data.model.UserRegistrationData

sealed class UploadAvatarEvent {
    data class RegisterUser(val user: UserRegistrationData) : UploadAvatarEvent()
    data object OnBackToEmptyRegisterState : UploadAvatarEvent()
    data class OnUploadImage(val image: String) : UploadAvatarEvent()
    data object OnBackToEmptyUploadAvatar : UploadAvatarEvent()
}
