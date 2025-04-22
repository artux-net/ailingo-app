package org.ailingo.app.features.topics.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.emptystate
import ailingo.composeapp.generated.resources.loadingstate
import ailingo.composeapp.generated.resources.topic_list_empty
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import org.ailingo.app.core.presentation.EmptyScreen
import org.ailingo.app.core.presentation.ErrorScreen
import org.ailingo.app.core.presentation.LoadingScreen
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.topics.data.model.Topic

@Composable
fun TopicsScreen(
    topicsUiState: UiState<List<Topic>>,
    onTopicClick: (String, String)->Unit
) {
    when (topicsUiState) {
        is UiState.Error -> {
            ErrorScreen(errorMessage = topicsUiState.message)
        }
        is UiState.Idle -> {}
        is UiState.Loading -> {
            LoadingScreen(modifier = Modifier.fillMaxSize(), image = Res.drawable.loadingstate)
        }
        is UiState.Success -> {
            if (topicsUiState.data.isEmpty()) {
                EmptyScreen(text = Res.string.topic_list_empty, modifier = Modifier.fillMaxSize(), image = Res.drawable.emptystate)
            } else {
                TopicsContent(topicsUiState.data, onTopicClick)
            }
        }
    }
}

@Composable
fun TopicsContent(
    topics: List<Topic>,
    onTopicClick: (String, String) -> Unit
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val adaptiveLazyGridSize = if (adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) 280.dp else 140.dp
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(adaptiveLazyGridSize),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(topics) { topic ->
            ContentTopics(topic, onTopicClick)
        }
    }
}