package org.ailingo.app.features.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.jwt.data.model.AuthResponse
import org.ailingo.app.features.registration.data.model.RegistrationRequest
import org.ailingo.app.features.registration.domain.repository.RegisterRepository
import org.ailingo.app.features.registration.domain.repository.VerifyEmailRepository

class RegisterUserViewModel(
    private val registerRepository: RegisterRepository,
    private val verificationRepository: VerifyEmailRepository
) : ViewModel() {
    private val _pendingRegistrationUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle())
    val pendingRegistrationUiState = _pendingRegistrationUiState.asStateFlow()

    private val _registrationUiState = MutableStateFlow<UiState<AuthResponse>>(UiState.Idle())
    val registrationUiState = _registrationUiState.asStateFlow()

    fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.OnRegisterUser -> registerUser(event.user)
            RegistrationEvent.OnBackToEmptyState -> resetState()
            is RegistrationEvent.OnVerifyEmail -> verifyEmail(event.email, event.verificationCode)
        }
    }

    private fun registerUser(user: RegistrationRequest) {
        viewModelScope.launch {
            registerRepository.register(user).collect { state ->
                _pendingRegistrationUiState.update { state }
            }
        }
    }

    private fun verifyEmail(email: String, verificationCode: String) {
        viewModelScope.launch {
            verificationRepository.verifyEmail(email, verificationCode).collect { state ->
                _registrationUiState.update { state }
            }
        }
    }

    private fun resetState() {
        _pendingRegistrationUiState.value = UiState.Idle()
    }
}