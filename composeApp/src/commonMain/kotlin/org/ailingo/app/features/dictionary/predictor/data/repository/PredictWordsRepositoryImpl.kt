package org.ailingo.app.features.dictionary.predictor.data.repository

import AiLingo.composeApp.BuildConfig.PREDICTOR_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.di.ErrorMapper
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorRequest
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorResponse
import org.ailingo.app.features.dictionary.predictor.domain.repository.PredictWordsRepository

class PredictWordsRepositoryImpl(
    private val httpClient: HttpClient,
    private val errorMapper: ErrorMapper
) : PredictWordsRepository {
    override fun predictNextWords(
        request: PredictorRequest
    ): Flow<UiState<PredictorResponse>> = flow {
        emit(UiState.Loading())
        try {
            val response = httpClient.post(PREDICTOR_BASE_URL) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(request)
            }.body<PredictorResponse>()
            emit(UiState.Success(response))
        } catch (e: Exception) {
            errorMapper.mapError(e)
        }
    }
}