package org.ailingo.app.features.login.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ailingo.app.features.login.presentation.LoginUiState

interface LoginRepository {
    fun loginUser(login: String, password: String): Flow<LoginUiState>
    fun autoLogin(): Flow<LoginUiState>
    suspend fun backToEmptyState()
}