package org.ailingo.app.features.favouritewords.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.empty_favourite_words
import ailingo.composeapp.generated.resources.emptyscreen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ailingo.app.core.presentation.EmptyScreen
import org.ailingo.app.core.presentation.ErrorScreen
import org.ailingo.app.core.presentation.LoadingScreen
import org.ailingo.app.core.presentation.UiState

@Composable
fun FavouriteScreen(
    favouriteWordsState: UiState<List<String>>,
    onNavigateToDictionaryScreen: (word: String) -> Unit,
    onEvent: (FavouriteWordsEvent) -> Unit
) {
    when (favouriteWordsState) {
        is UiState.Error -> {
            ErrorScreen(favouriteWordsState.message, modifier = Modifier.fillMaxSize())
        }

        is UiState.Idle -> {}
        is UiState.Loading -> {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }

        is UiState.Success -> {
            if (favouriteWordsState.data.isEmpty()) {
                EmptyScreen(text = Res.string.empty_favourite_words, modifier = Modifier.fillMaxSize(), image = Res.drawable.emptyscreen)
            } else {
                FavouriteWordsContent(favouriteWordsState.data, onNavigateToDictionaryScreen = {
                    onNavigateToDictionaryScreen(it)
                }, onEvent = onEvent)
            }
        }
    }
}

@Composable
fun FavouriteWordsContent(
    words: List<String>,
    onNavigateToDictionaryScreen: (word: String) -> Unit,
    onEvent: (FavouriteWordsEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(words) { word ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = word, modifier = Modifier
                            .clickable(onClick = {
                                onNavigateToDictionaryScreen(word)
                            }).padding(start = 16.dp),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        onNavigateToDictionaryScreen(word)
                    }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { onEvent(FavouriteWordsEvent.OnDeleteFavouriteWord(word)) }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Remove")
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}