package org.ailingo.app.features.jwt.domain.repository

import org.ailingo.app.features.jwt.data.model.RefreshTokenResponse
import org.ailingo.app.features.jwt.data.model.Token

interface TokenRepository {
    suspend fun saveTokens(tokensInfo: RefreshTokenResponse)
    suspend fun getTokens(): Token?
    suspend fun updateTokens(newToken: String, newRefreshToken: String, oldRefreshToken: String)
    suspend fun deleteToken(token: String)
    suspend fun deleteRefreshToken(refreshToken: String)
    suspend fun deleteTokens()
}