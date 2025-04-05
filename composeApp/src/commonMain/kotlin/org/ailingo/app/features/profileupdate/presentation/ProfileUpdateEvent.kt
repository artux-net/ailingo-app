package org.ailingo.app.features.profileupdate.presentation

import org.ailingo.app.features.profileupdate.data.model.ProfileUpdateRequest

sealed class ProfileUpdateEvent {
    data class OnUpdateProfile(
        val profileUpdateRequest: ProfileUpdateRequest
    ) : ProfileUpdateEvent()

    data object OnBackToEmptyState : ProfileUpdateEvent()

    data class OnUploadAvatar(val imageBase64: String) : ProfileUpdateEvent()
}