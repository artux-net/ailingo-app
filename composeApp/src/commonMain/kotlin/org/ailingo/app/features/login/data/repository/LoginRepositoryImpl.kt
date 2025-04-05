package org.ailingo.app.features.login.data.repository

import AiLingo.composeApp.BuildConfig.API_ENDPOINT_USER
import AiLingo.composeApp.BuildConfig.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ailingo.app.di.ErrorMapper
import org.ailingo.app.features.jwt.data.model.AuthResponse
import org.ailingo.app.features.jwt.data.model.RefreshTokenRequest
import org.ailingo.app.features.jwt.data.model.RefreshTokenResponse
import org.ailingo.app.features.jwt.domain.repository.TokenRepository
import org.ailingo.app.features.login.data.model.LoginRequestBody
import org.ailingo.app.features.login.data.model.User
import org.ailingo.app.features.login.domain.repository.LoginRepository
import org.ailingo.app.features.login.presentation.LoginUiState

class LoginRepositoryImpl(
    private val httpClient: HttpClient,
    private val errorMapper: ErrorMapper,
    private val tokenRepositoryDeferred: Deferred<TokenRepository>
) : LoginRepository {
    override fun loginUser(login: String, password: String): Flow<LoginUiState> = flow {
        emit(LoginUiState.Loading)
        try {
            val response = httpClient.post("$BASE_URL$API_ENDPOINT_USER/login") {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(LoginRequestBody(login, password))
            }
            if (response.status.isSuccess()) {
                val loginResponse = response.body<AuthResponse>()
                emit(LoginUiState.Success(loginResponse.user, loginResponse.token, loginResponse.refreshToken))
                tokenRepositoryDeferred.await().saveTokens(RefreshTokenResponse(loginResponse.token, loginResponse.refreshToken))
            } else {
                emit(LoginUiState.Error(errorMapper.mapError(httpResponse = response)))
            }
        } catch (e: Exception) {
            emit(LoginUiState.Error(errorMapper.mapError(e)))
        }
    }

    override fun autoLogin(): Flow<LoginUiState> = flow {
        val tokenRepository = tokenRepositoryDeferred.await()
        try {
            val tokens = tokenRepository.getTokens()
            if (tokens != null) {
                val infoResponse = httpClient.get("$BASE_URL$API_ENDPOINT_USER/info") {
                    header(HttpHeaders.Authorization, "Bearer ${tokens.token}")
                }

                if (infoResponse.status.isSuccess()) {
                    val authInfo = infoResponse.body<User>()
                    emit(
                        LoginUiState.Success(
                            authInfo,
                            token = tokens.token,
                            refreshToken = tokens.refreshToken
                        )
                    )
                } else {
                    if (infoResponse.status == HttpStatusCode.Unauthorized || infoResponse.status == HttpStatusCode.Forbidden) {
                        val refreshTokenResponse = try {
                            httpClient.post("$BASE_URL$API_ENDPOINT_USER/refreshToken") {
                                header(HttpHeaders.ContentType, ContentType.Application.Json)
                                setBody(RefreshTokenRequest(refreshToken = tokens.refreshToken))
                            }
                        } catch (e: Exception) {
                            tokenRepository.deleteTokens()
                            emit(LoginUiState.Unauthenticated)
                            return@flow
                        }

                        if (refreshTokenResponse.status.isSuccess()) {
                            val refreshResponse = refreshTokenResponse.body<RefreshTokenResponse>()
                            tokenRepository.saveTokens(refreshResponse)

                            val newInfoResponse = httpClient.get("$BASE_URL$API_ENDPOINT_USER/info") {
                                header(HttpHeaders.Authorization, "Bearer ${refreshResponse.accessToken}")
                            }

                            if (newInfoResponse.status.isSuccess()) {
                                val authInfo = newInfoResponse.body<User>()
                                emit(
                                    LoginUiState.Success(
                                        authInfo,
                                        token = refreshResponse.accessToken,
                                        refreshToken = refreshResponse.refreshToken
                                    )
                                )
                            } else {
                                tokenRepository.deleteTokens()
                                emit(LoginUiState.Unauthenticated)
                            }

                        } else {
                            tokenRepository.deleteTokens()
                            emit(LoginUiState.Unauthenticated)
                        }
                    } else {
                        tokenRepository.deleteTokens()
                        emit(LoginUiState.Unauthenticated)
                    }
                }
            } else {
                tokenRepository.deleteTokens()
                emit(LoginUiState.Unauthenticated)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            tokenRepository.deleteTokens()
            emit(LoginUiState.Unauthenticated)
        }
    }

    override suspend fun backToEmptyState() {
        val tokenRepository = tokenRepositoryDeferred.await()
        tokenRepository.deleteTokens()
    }
}