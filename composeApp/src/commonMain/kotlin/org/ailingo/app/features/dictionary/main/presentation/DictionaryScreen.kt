package org.ailingo.app.features.dictionary.main.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.definitions
import ailingo.composeapp.generated.resources.history_of_search
import ailingo.composeapp.generated.resources.loadingstate
import ailingo.composeapp.generated.resources.usage_examples
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.ailingo.app.core.presentation.ErrorScreen
import org.ailingo.app.core.presentation.LoadingScreen
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.dictionary.historysearch.data.model.DictionarySearchHistory
import org.ailingo.app.features.dictionary.main.data.model.DictionaryData
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorResponse
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DictionaryScreen(
    dictionaryState: UiState<DictionaryData>,
    searchHistoryState: UiState<List<DictionarySearchHistory>>,
    favoriteDictionaryState: UiState<List<String>>,
    predictorState: UiState<PredictorResponse>,
    onEvent: (DictionaryEvents) -> Unit
) {
    var textFieldValue by remember {
        mutableStateOf("")
    }
    val active = remember {
        mutableStateOf(false)
    }
    val searchBarHeight = remember { mutableStateOf(0) }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            stickyHeader {
                SearchTextFieldDictionary(
                    predictorState = predictorState,
                    textFieldValue = textFieldValue,
                    onTextFieldValueChange = { newTextFieldValue ->
                        textFieldValue = newTextFieldValue
                    },
                    active = active,
                    searchBarHeight = searchBarHeight,
                    onSearchClick = { searchWord ->
                        onEvent(DictionaryEvents.GetWordInfo(searchWord))
                    },
                    onPredictWords = { chars ->
                        onEvent(DictionaryEvents.PredictNextWords(chars))
                    },
                    onSaveSearchedWord = { word ->
                        onEvent(DictionaryEvents.SaveSearchedWord(word))
                    }
                )
            }
            if (dictionaryState is UiState.Idle) {
                when (searchHistoryState) {
                    is UiState.Error -> {
                        item {
                            ErrorScreen(searchHistoryState.message)
                        }
                    }

                    is UiState.Loading -> {
                        item {
                            LoadingScreen(modifier = Modifier.fillMaxSize(), image = Res.drawable.loadingstate)
                        }
                    }

                    is UiState.Success -> {
                        if (searchHistoryState.data.isEmpty()) {
                            item {
                                Text(stringResource(Res.string.history_of_search), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                            }
                        } else {
                            items(searchHistoryState.data.reversed()) { searchHistoryItem ->
                                SearchHistoryItem(
                                    searchHistoryItem = searchHistoryItem,
                                    onGetWordInfo = { searchWord ->
                                        onEvent(DictionaryEvents.GetWordInfo(searchWord))
                                    },
                                    onTextFieldChange = { text ->
                                        textFieldValue = text
                                    },
                                    onActiveChange = {
                                        active.value = it
                                    }
                                )
                            }
                        }
                    }

                    is UiState.Idle -> {}
                }
            }
            when (dictionaryState) {
                is UiState.Error -> {
                    item { ErrorScreen(errorMessage = dictionaryState.message, modifier = Modifier.fillMaxSize()) }
                }

                is UiState.Idle -> {}
                is UiState.Loading -> {
                    item { LoadingScreen(modifier = Modifier.fillMaxSize(), image = Res.drawable.loadingstate) }
                }

                is UiState.Success -> {
                    val listOfExamples = dictionaryState.data.dictionaryApiDevResponses.flatMap {
                        it.meanings.flatMap { meaning ->
                            meaning.definitions.mapNotNull { def ->
                                def.example
                            }
                        }
                    }
                    val listOfDefinitions = dictionaryState.data.dictionaryApiDevResponses.flatMap {
                        it.meanings.flatMap { meaning ->
                            meaning.definitions.map { def ->
                                def.definition
                            }
                        }
                    }
                    items(dictionaryState.data.yandexDictionaryResponse.def) { definition ->
                        WordHeader(
                            word = definition.text,
                            trans = definition.tr.joinToString { it.text },
                            audio = dictionaryState.data.dictionaryApiDevResponses.first().phonetics.first().audio,
                            partOfSpeech = definition.pos,
                            onEvent = onEvent,
                            favoriteDictionaryState = favoriteDictionaryState
                        )
                        definition.tr.forEachIndexed { index, tr ->
                            DefinitionEntry(index, tr)
                        }
                    }
                    item {
                        if (listOfExamples.isNotEmpty()) {
                            Text(
                                stringResource(Res.string.usage_examples),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    item {
                        ListOfExample(listOfExamples, textFieldValue)
                    }
                    item {
                        if (listOfDefinitions.isNotEmpty()) {
                            Text(
                                stringResource(Res.string.definitions),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    item {
                        ListOfDefinitions(listOfDefinitions)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchHistoryItem(
    searchHistoryItem: DictionarySearchHistory,
    onGetWordInfo: (String) -> Unit,
    onTextFieldChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(14.dp).clickable {
            onTextFieldChange(searchHistoryItem.text)
            onGetWordInfo(searchHistoryItem.text)
            onActiveChange(false)
        }
    ) {
        Icon(
            modifier = Modifier.padding(end = 10.dp),
            imageVector = Icons.Default.History,
            contentDescription = null
        )
        Text(text = searchHistoryItem.text)
    }
}