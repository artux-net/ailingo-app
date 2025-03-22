package org.ailingo.app.features.jwt.domain.repository

import org.ailingo.app.features.jwt.data.model.AuthResponse
import org.ailingo.app.features.jwt.data.model.JwtInfo

interface TokenRepository {
    suspend fun saveTokens(authResponse: AuthResponse)
    suspend fun getTokens(): JwtInfo?
    suspend fun updateTokens(newToken: String, newRefreshToken: String, oldRefreshToken: String)
    suspend fun deleteToken(token: String)
    suspend fun deleteRefreshToken(refreshToken: String)
    suspend fun deleteTokens()
}