package org.ailingo.app.feature_register.data.model_upload_image

sealed class UploadImageUiState {
    data class Success(val uploadImageResponse: UploadImageResponse): UploadImageUiState()
    data class Error(val message: String): UploadImageUiState()
    object EmptyImage: UploadImageUiState()
    object LoadingImage: UploadImageUiState()
}