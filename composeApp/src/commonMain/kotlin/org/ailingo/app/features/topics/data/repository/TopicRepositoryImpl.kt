package org.ailingo.app.features.topics.data.repository

import AiLingo.composeApp.BuildConfig.API_ENDPOINT_TOPICS
import AiLingo.composeApp.BuildConfig.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.di.ErrorMapper
import org.ailingo.app.features.topics.data.model.Topic
import org.ailingo.app.features.topics.domain.repository.TopicRepository

class TopicRepositoryImpl(
    private val httpClient: HttpClient,
    private val errorMapper: ErrorMapper
): TopicRepository {
    override fun getTopics(): Flow<UiState<List<Topic>>> = flow {
        emit(UiState.Loading())
        try {
            val response = httpClient.get("$BASE_URL$API_ENDPOINT_TOPICS/getTopics") {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                val topicResponse = response.body<List<Topic>>()
                emit(UiState.Success(topicResponse))
            } else {
                emit(UiState.Error(errorMapper.mapError(httpResponse = response)))
            }
        } catch (e: Exception) {
            emit(UiState.Error(errorMapper.mapError(e)))
        }
    }
}