package org.ailingo.app.features.dictionary.main.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.definitions
import ailingo.composeapp.generated.resources.no_definitions
import ailingo.composeapp.generated.resources.usage_examples
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import org.ailingo.app.core.presentation.ErrorScreen
import org.ailingo.app.core.presentation.LoadingScreen
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.dictionary.examples.data.model.WordInfoItem
import org.ailingo.app.features.dictionary.historysearch.data.model.DictionarySearchHistory
import org.ailingo.app.features.dictionary.main.data.model.DictionaryResponse
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorResponse
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DictionaryScreen(
    dictionaryState: UiState<DictionaryResponse>,
    examplesState: UiState<List<WordInfoItem>>,
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
                            LoadingScreen(modifier = Modifier.fillMaxSize())
                        }
                    }

                    is UiState.Success -> {
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

                    is UiState.Idle -> {}
                }
            }
            when (examplesState) {
                is UiState.Error -> {
                    item { ErrorScreen(modifier = Modifier.fillMaxSize()) }
                }

                is UiState.Idle -> {}
                is UiState.Loading -> {
                    item { LoadingScreen(modifier = Modifier.fillMaxSize()) }
                }

                is UiState.Success -> {
                    val listOfExamples = examplesState.data.flatMap {
                        it.meanings.flatMap { meaning ->
                            meaning.definitions.mapNotNull { def ->
                                def.example
                            }
                        }
                    }
                    val listOfDefinitions = examplesState.data.flatMap {
                        it.meanings.flatMap { meaning ->
                            meaning.definitions.mapNotNull { def ->
                                def.definition
                            }
                        }
                    }
                    if (dictionaryState is UiState.Success) {
                        if (dictionaryState.data.definitions.isNotEmpty()) {
                            items(dictionaryState.data.definitions) { definition ->
                                DefinitionRowInfo(
                                    definition,
                                    examplesState.data,
                                    favoriteDictionaryState,
                                    onEvent = onEvent
                                )
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
                        } else {
                            item {
                                Text(stringResource(Res.string.no_definitions))
                            }
                        }
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