package org.ailingo.app.features.registration.presentation.uploadavatar

import org.ailingo.app.features.registration.data.image.UploadImageResponse

sealed class UploadImageUiState {
    data class Success(val uploadImageResponse: UploadImageResponse) : UploadImageUiState()
    data class Error(val message: String) : UploadImageUiState()
    object EmptyImage : UploadImageUiState()
    object LoadingImage : UploadImageUiState()
}
