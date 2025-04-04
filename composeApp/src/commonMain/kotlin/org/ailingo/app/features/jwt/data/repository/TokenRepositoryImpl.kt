package org.ailingo.app.features.jwt.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.ailingo.app.AppDatabase
import org.ailingo.app.features.jwt.data.model.AuthResponse
import org.ailingo.app.features.jwt.data.model.JwtInfo
import org.ailingo.app.features.jwt.domain.repository.TokenRepository

class TokenRepositoryImpl(
    database: AppDatabase
) : TokenRepository {

    private val tokenQueries = database.tokenQueries

    override suspend fun saveTokens(authResponse: AuthResponse) {
        tokenQueries.transaction {
            tokenQueries.deleteTokens()
            tokenQueries.insertTokens(
                token = authResponse.token,
                refreshToken = authResponse.refreshToken
            )
        }
    }

    override suspend fun getTokens(): JwtInfo? {
        return tokenQueries
            .getTokens()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { tokenEntities ->
                tokenEntities.firstOrNull()?.let {
                    JwtInfo(
                        id = it.id,
                        token = it.token,
                        refreshToken = it.refresh_token
                    )
                }
            }
            .firstOrNull()
    }

    override suspend fun updateTokens(
        newToken: String,
        newRefreshToken: String,
        oldRefreshToken: String
    ) {
        tokenQueries.updateTokens(
            newToken = newToken,
            newRefreshToken = newRefreshToken,
            oldRefreshToken = oldRefreshToken
        )
    }

    override suspend fun deleteToken(token: String) {
        tokenQueries.deleteToken(token)
    }

    override suspend fun deleteRefreshToken(refreshToken: String) {
        tokenQueries.deleteRefreshToken(refreshToken)
    }

    override suspend fun deleteTokens() {
        tokenQueries.deleteTokens()
    }
}