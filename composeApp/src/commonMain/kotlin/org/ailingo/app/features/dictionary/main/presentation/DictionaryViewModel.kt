package org.ailingo.app.features.dictionary.main.presentation

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

    private val _historyOfDictionaryState = MutableStateFlow<HistoryDictionaryUiState>(HistoryDictionaryUiState.Empty)
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

    private val apiKeyDictionary =
        "dict.1.1.20231102T140345Z.9979700cf66f91d0.b210308b827953080f07e8f2e12779e2486d2695"
    private val baseUrl = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup"
    private val baseFreeDictionaryUrl = "https://api.dictionaryapi.dev/api/v2/entries/en"

    private val _uiState = MutableStateFlow<DictionaryUiState>(DictionaryUiState.Empty)
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    private val predictorBaseUrl = "https://api.typewise.ai/latest/completion/complete"

    private var _items = MutableStateFlow<PredictorResponse?>(null)
    val items: StateFlow<PredictorResponse?> = _items.asStateFlow()

    fun onEvent(event: DictionaryScreenEvents) {
        when (event) {
            is DictionaryScreenEvents.PredictNextWords -> {
                viewModelScope.launch {
                    val response = httpClient.post(predictorBaseUrl) {
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
                                httpClient.get("$baseUrl?key=$apiKeyDictionary&lang=en-ru&text=${event.word}")
                                    .body<DictionaryResponse>()
                            } catch (e: Exception) {
                                DictionaryResponse(emptyList())
                            }
                        }

                        val deferredResponseExample = async {
                            try {
                                httpClient.get("$baseFreeDictionaryUrl/${event.word}")
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
