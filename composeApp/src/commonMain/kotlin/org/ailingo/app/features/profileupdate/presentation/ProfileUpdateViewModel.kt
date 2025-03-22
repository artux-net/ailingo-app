package org.ailingo.app.features.profileupdate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.profileupdate.data.model.ProfileUpdateRequest
import org.ailingo.app.features.profileupdate.data.model.ProfileUpdateResponse
import org.ailingo.app.features.profileupdate.domain.repository.ProfileUpdateRepository

class ProfileUpdateViewModel(
    private val profileUpdateRepository: ProfileUpdateRepository
) : ViewModel() {
    private val _profileUpdateUiState = MutableStateFlow<UiState<ProfileUpdateResponse>>(UiState.Idle())
    val profileUpdateUiState = _profileUpdateUiState.asStateFlow()

    fun onEvent(event: ProfileUpdateEvent) {
        when (event) {
            is ProfileUpdateEvent.OnUpdateProfile -> {
                updateProfile(event.profileUpdateRequest)
            }

            ProfileUpdateEvent.OnBackToEmptyState -> {
                backToEmptyState()
            }
        }
    }

    private fun updateProfile(profileUpdateRequest: ProfileUpdateRequest) {
        viewModelScope.launch {
            profileUpdateRepository.updateProfile(profileUpdateRequest).collect { state ->
                _profileUpdateUiState.update { state }
            }
        }
    }

    private fun backToEmptyState() {
        _profileUpdateUiState.value = UiState.Idle()
    }
}