package org.ailingo.app.features.dictionary.main.data.repository

import AiLingo.composeApp.BuildConfig.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.di.ErrorMapper
import org.ailingo.app.features.dictionary.main.data.model.DictionaryData
import org.ailingo.app.features.dictionary.main.domain.repository.DictionaryRepository

class DictionaryRepositoryImpl(
    private val httpClient: HttpClient,
    private val errorMapper: ErrorMapper
) : DictionaryRepository {
    override fun getWordInfo(word: String): Flow<UiState<DictionaryData>> = flow {
        emit(UiState.Loading())
        try {
            val response = httpClient.get("$BASE_URL/api/v1/dictionary/define") {
                parameter("word", word)
            }.body<DictionaryData>()
            emit(UiState.Success(response))
        } catch (e: Exception) {
            emit(UiState.Error(errorMapper.mapError(e)))
        }
    }
}