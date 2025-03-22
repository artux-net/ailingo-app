package org.ailingo.app.features.dictionary.examples.data.repository

import AiLingo.composeApp.BuildConfig.BASE_URL_FREE_DICTIONARY
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.di.ErrorMapper
import org.ailingo.app.features.dictionary.examples.data.model.WordInfoItem
import org.ailingo.app.features.dictionary.examples.domain.repository.DictionaryExampleRepository

class DictionaryExampleRepositoryImpl(
    private val httpClient: HttpClient,
    private val errorMapper: ErrorMapper
) : DictionaryExampleRepository {
    override fun getExamples(word: String): Flow<UiState<List<WordInfoItem>>> = flow {
        emit(UiState.Loading())
        try {
            val response = httpClient.get("$BASE_URL_FREE_DICTIONARY/$word")
                .body<List<WordInfoItem>>()
            emit(UiState.Success(response))
        } catch (e: Exception) {
            emit(UiState.Error(errorMapper.mapError(e)))
        }
    }
}