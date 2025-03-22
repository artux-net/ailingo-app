package org.ailingo.app.features.registration.presentation

import org.ailingo.app.features.registration.data.model.RegistrationRequest

sealed class RegistrationEvent {
    data class OnRegisterUser(val user: RegistrationRequest) : RegistrationEvent()
    data class OnVerifyEmail(val email: String, val verificationCode: String) : RegistrationEvent()
    data object OnBackToEmptyState : RegistrationEvent()
}