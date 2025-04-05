package org.ailingo.app.features.profileupdate.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.profileupdate.data.model.ProfileUpdateRequest
import org.ailingo.app.features.profileupdate.data.model.ProfileUpdateResponse
import org.ailingo.app.features.profileupdate.data.model.imageuploader.ImageUploaderResponse

interface ProfileUpdateRepository {
    fun updateProfile(profileUpdateRequest: ProfileUpdateRequest): Flow<UiState<ProfileUpdateResponse>>
    fun uploadAvatar(imageBase64: String): Flow<UiState<ImageUploaderResponse>>
}