package org.ailingo.app.feature_dictionary.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search
import kotlinx.coroutines.delay
import org.ailingo.app.feature_dicitionary_predictor.data.PredictorRequest
import org.ailingo.app.feature_dictionary_history.domain.HistoryDictionary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextFieldDictionary(
    component: DictionaryScreenComponent,
    textFieldValue: MutableState<String>,
    onTextFieldValueChange: (String) -> Unit,
    active: MutableState<Boolean>,
    searchBarHeight: MutableState<Int>,
    onSearchClick: (String) -> Unit
) {
    val items = component.items.collectAsState()
    LaunchedEffect(textFieldValue.value) {
        val trimmedText = textFieldValue.value.trim()
        if (trimmedText.isNotBlank()) {
            delay(250)
            println(trimmedText)
            if (active.value) {
                 component.onEvent(DictionaryScreenEvents.PredictNextWords(
                    PredictorRequest(
                        false,
                        listOf("en"),
                        5,
                        trimmedText,
                        "string"
                    )
                ))
            }
        }
    }
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        DockedSearchBar(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).onGloballyPositioned {
                searchBarHeight.value = it.size.height
            },
            query = textFieldValue.value,
            onQueryChange = onTextFieldValueChange,
            onSearch = {
                onSearchClick(it)
                component.onEvent(DictionaryScreenEvents.SaveSearchedWord(HistoryDictionary(null,it)))
                active.value = false
            },
            active = active.value,
            onActiveChange = {
                active.value = it
            },
            placeholder = {
                Text("Search")
            },
            leadingIcon = {
                Icon(imageVector = FeatherIcons.Search, contentDescription = null)
            },
            trailingIcon = {
                if (active.value) {
                    Icon(modifier = Modifier.clickable {
                        if (textFieldValue.value.isNotEmpty()) {
                            textFieldValue.value = ""
                        } else {
                            active.value = false
                        }
                    }, imageVector = Icons.Filled.Close, contentDescription = null)
                }
            }
        ) {
            items.value?.predictions?.let { predictions ->
                val uniqueWords = predictions
                    .map { it.text }
                    .distinct()

                uniqueWords.forEach { uniqueWord ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp).clickable {
                            textFieldValue.value = uniqueWord
                            component.onEvent(DictionaryScreenEvents.SaveSearchedWord(
                                HistoryDictionary(null,uniqueWord)
                            ))
                            onSearchClick(uniqueWord)
                            active.value = false
                        }) {
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
    }
}