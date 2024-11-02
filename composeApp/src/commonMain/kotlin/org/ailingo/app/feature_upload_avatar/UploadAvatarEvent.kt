package org.ailingo.app.feature_upload_avatar

import org.ailingo.app.feature_register.data.model.UserRegistrationData

sealed class UploadAvatarEvent {
    data class RegisterUser(val user: UserRegistrationData): UploadAvatarEvent()
    data object OnBackToEmptyRegisterState: UploadAvatarEvent()
    data class OnUploadImage(val image: String): UploadAvatarEvent()
    data object OnBackToEmptyUploadAvatar: UploadAvatarEvent()
}