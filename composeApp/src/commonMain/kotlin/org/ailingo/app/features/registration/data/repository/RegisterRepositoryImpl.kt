package org.ailingo.app.features.registration.data.repository

import AiLingo.composeApp.BuildConfig.API_ENDPOINT_USER
import AiLingo.composeApp.BuildConfig.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.di.ErrorMapper
import org.ailingo.app.features.registration.data.model.RegistrationRequest
import org.ailingo.app.features.registration.domain.repository.RegisterRepository

class RegisterRepositoryImpl(
    private val httpClient: HttpClient,
    private val errorParser: ErrorMapper,
) : RegisterRepository {
    override fun register(registrationRequest: RegistrationRequest): Flow<UiState<Unit>> = flow {
        emit(UiState.Loading())
        try {
            val response = httpClient.post("$BASE_URL$API_ENDPOINT_USER/register") {
                contentType(ContentType.Application.Json)
                setBody(registrationRequest)
            }
            if (response.status.isSuccess()) {
                emit(UiState.Success(Unit))
            } else {
                emit(UiState.Error(errorParser.mapError(httpResponse = response)))
            }
        } catch (e: Exception) {
            emit(UiState.Error(errorParser.mapError(e)))
        }
    }
}