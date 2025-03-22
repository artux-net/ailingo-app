package org.ailingo.app.features.dictionary.main.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.search
import ailingo.composeapp.generated.resources.word_not_found
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search
import kotlinx.coroutines.delay
import org.ailingo.app.core.presentation.ErrorScreen
import org.ailingo.app.core.presentation.LoadingScreen
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.dictionary.historysearch.data.model.DictionarySearchHistory
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorRequest
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorResponse
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextFieldDictionary(
    predictorState: UiState<PredictorResponse>,
    textFieldValue: String,
    onTextFieldValueChange: (String) -> Unit,
    active: MutableState<Boolean>,
    searchBarHeight: MutableState<Int>,
    onSearchClick: (String) -> Unit,
    onPredictWords: (request: PredictorRequest) -> Unit,
    onSaveSearchedWord: (DictionarySearchHistory) -> Unit
) {
    LaunchedEffect(textFieldValue) {
        onTextFieldValueChange(textFieldValue.trim())
        if (textFieldValue.isNotBlank()) {
            delay(250)
            if (active.value) {
                onPredictWords(
                    PredictorRequest(
                        false,
                        listOf("en"),
                        5,
                        textFieldValue,
                        "string"
                    )
                )
            }
        }
    }
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        DockedSearchBar(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).onGloballyPositioned {
                searchBarHeight.value = it.size.height
            },
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = Modifier.fillMaxWidth(),
                    query = textFieldValue,
                    onQueryChange = {
                        onTextFieldValueChange(it)
                    },
                    onSearch = {
                        onSearchClick(it)
                        onSaveSearchedWord(
                            DictionarySearchHistory(null, it)
                        )
                        active.value = false
                    },
                    expanded = active.value,
                    onExpandedChange = { active.value = it },
                    placeholder = { Text(stringResource(Res.string.search)) },
                    leadingIcon = { Icon(FeatherIcons.Search, contentDescription = null) },
                    trailingIcon = {
                        if (active.value) {
                            IconButton(
                                onClick = {
                                    if (textFieldValue.isNotEmpty()) {
                                        onTextFieldValueChange("")
                                    } else {
                                        active.value = false
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            },
            expanded = active.value,
            onExpandedChange = {
                active.value = it
            },
        ) {
            when (predictorState) {
                is UiState.Error -> {
                    ErrorScreen(predictorState.message)
                }
                is UiState.Idle -> {}
                is UiState.Loading -> {
                    LoadingScreen(modifier = Modifier.fillMaxWidth().height(100.dp))
                }
                is UiState.Success -> {
                    if (predictorState.data.predictions.isNotEmpty()) {
                        PredictorContent(
                            predictorState.data,
                            onTextFieldValueChange = onTextFieldValueChange,
                            onSaveSearchedWord = onSaveSearchedWord,
                            onSearchClick = onSearchClick,
                            active = active
                        )
                    } else {
                        Text(
                            stringResource(Res.string.word_not_found),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PredictorContent(
    predictorItems: PredictorResponse,
    onTextFieldValueChange: (String) -> Unit,
    onSaveSearchedWord: (DictionarySearchHistory) -> Unit,
    onSearchClick: (String) -> Unit,
    active: MutableState<Boolean>
) {
    predictorItems.predictions.let { predictions ->
        val uniqueWords = predictions
            .map { it.text }
            .distinct()

        uniqueWords.forEach { uniqueWord ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(14.dp).clickable {
                    onTextFieldValueChange(uniqueWord)
                    onSaveSearchedWord(DictionarySearchHistory(null, uniqueWord))
                    onSearchClick(uniqueWord)
                    active.value = false
                }
            ) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = FeatherIcons.Search,
                    contentDescription = null
                )
                Text(text = uniqueWord)
            }
        }
    }
}