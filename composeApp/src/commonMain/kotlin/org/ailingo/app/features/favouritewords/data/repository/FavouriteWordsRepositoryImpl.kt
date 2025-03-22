package org.ailingo.app.features.favouritewords.data.repository

import AiLingo.composeApp.BuildConfig.BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.di.ErrorMapper
import org.ailingo.app.features.favouritewords.domain.repository.FavouriteWordsRepository

class FavouriteWordsRepositoryImpl(
    private val httpClient: HttpClient,
    private val errorMapper: ErrorMapper
) : FavouriteWordsRepository {

    override fun getFavouriteWords(): Flow<UiState<List<String>>> = flow {
        emit(UiState.Loading())
        try {
            val response = httpClient.get("$BASE_URL/api/v1/words/favorites") {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                val wordsBody = response.body<List<String>>()
                emit(UiState.Success(wordsBody))
            } else {
                emit(UiState.Error(errorMapper.mapError(httpResponse = response)))
            }
        } catch (e: Exception) {
            emit(UiState.Error(errorMapper.mapError(e)))
        }
    }

    override suspend fun addFavouriteWord(word: String) {
        httpClient.post("$BASE_URL/api/v1/words/favorites/$word")
    }

    override suspend fun deleteFavouriteWord(word: String) {
        httpClient.delete("$BASE_URL/api/v1/words/favorites/$word")
    }
}