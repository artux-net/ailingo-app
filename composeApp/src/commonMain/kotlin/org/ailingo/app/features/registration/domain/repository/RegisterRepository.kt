package org.ailingo.app.features.registration.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.registration.data.model.RegistrationRequest

interface RegisterRepository {
    fun register(registrationRequest: RegistrationRequest): Flow<UiState<Unit>>
}