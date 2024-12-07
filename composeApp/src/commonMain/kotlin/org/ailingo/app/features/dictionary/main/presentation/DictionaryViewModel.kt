package org.ailingo.app.features.dictionary.main.presentation

import AiLingo.composeApp.BuildConfig.API_KEY_DICTIONARY
import AiLingo.composeApp.BuildConfig.BASE_URL_FREE_DICTIONARY
import AiLingo.composeApp.BuildConfig.BASE_URL_YANEX_DICTIONARY
import AiLingo.composeApp.BuildConfig.PREDICTOR_BASE_URL
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.ailingo.app.features.dictionary.examples.data.model.WordInfoItem
import org.ailingo.app.features.dictionary.history.domain.DictionaryRepository
import org.ailingo.app.features.dictionary.history.domain.HistoryDictionaryUiState
import org.ailingo.app.features.dictionary.main.data.model.DictionaryResponse
import org.ailingo.app.features.dictionary.predictor.data.PredictorResponse

class DictionaryViewModel(
    private val historyDictionaryRepository: Deferred<DictionaryRepository>
) : ViewModel() {

    private val _historyOfDictionaryState = MutableStateFlow<HistoryDictionaryUiState>(HistoryDictionaryUiState.Loading)
    val historyOfDictionaryState = _historyOfDictionaryState.asStateFlow()

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    init {
        loadHistory()
    }

    private fun loadHistory() {
        _historyOfDictionaryState.value = HistoryDictionaryUiState.Loading
        viewModelScope.launch {
            historyDictionaryRepository.await().getDictionaryHistory().onEach { history ->
                _historyOfDictionaryState.value = HistoryDictionaryUiState.Success(history)
            }.launchIn(viewModelScope)
        }
    }

    private val _uiState = MutableStateFlow<DictionaryUiState>(DictionaryUiState.Empty)
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    private var _items = MutableStateFlow<PredictorResponse?>(null)
    val items: StateFlow<PredictorResponse?> = _items.asStateFlow()

    fun onEvent(event: DictionaryScreenEvents) {
        when (event) {
            is DictionaryScreenEvents.PredictNextWords -> {
                viewModelScope.launch {
                    val response = httpClient.post(PREDICTOR_BASE_URL) {
                        header(HttpHeaders.ContentType, ContentType.Application.Json)
                        setBody(event.request)
                    }
                    _items.update {
                        response.body<PredictorResponse>()
                    }
                }
            }

            is DictionaryScreenEvents.SaveSearchedWord -> {
                viewModelScope.launch {
                    try {
                        historyDictionaryRepository.await().insertWordToHistory(event.word)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is DictionaryScreenEvents.SearchWordDefinition -> {
                viewModelScope.launch {
                    try {
                        _uiState.value = DictionaryUiState.Loading

                        val deferredResponse = async {
                            try {
                                httpClient.get("$BASE_URL_YANEX_DICTIONARY?key=$API_KEY_DICTIONARY&lang=en-ru&text=${event.word}")
                                    .body<DictionaryResponse>()
                            } catch (e: Exception) {
                                DictionaryResponse(emptyList())
                            }
                        }

                        val deferredResponseExample = async {
                            try {
                                httpClient.get("$BASE_URL_FREE_DICTIONARY/${event.word}")
                                    .body<List<WordInfoItem>>()
                            } catch (e: Exception) {
                                emptyList()
                            }
                        }
                        val response = deferredResponse.await()
                        val responseExample = deferredResponseExample.await()

                        _uiState.value = DictionaryUiState.Success(response, responseExample)
                    } catch (e: Exception) {
                        _uiState.value =
                            DictionaryUiState.Error("Error occurred while fetching data. ${e.message}")
                    }
                }
            }

            is DictionaryScreenEvents.DeleteFromHistory -> {
                viewModelScope.launch {
                    try {
                        historyDictionaryRepository.await().deleteWordFromHistory(event.id)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
