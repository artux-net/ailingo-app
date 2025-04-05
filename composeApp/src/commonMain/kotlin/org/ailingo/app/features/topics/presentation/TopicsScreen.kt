package org.ailingo.app.features.topics.presentation

import ailingo.composeapp.generated.resources.Res
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
import org.ailingo.app.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }
        is UiState.Success -> {
            if (topicsUiState.data.isEmpty()) {
                EmptyScreen(text = Res.string.topic_list_empty, modifier = Modifier.fillMaxSize())
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
        items(topics) { photo ->
            ContentTopics(photo, onTopicClick)
        }
    }
}

private fun createSampleTopics(): List<Topic> {
    return listOf(
        Topic(1, "Travel Adventures", "https://images.unsplash.com/photo-1503220718398-8a546943e773?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8dHJhdmVsfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80", 0, "Let's talk about your dream destinations!", "You are a travel expert.", 10),
        Topic(2, "Culinary Delights", "https://images.unsplash.com/photo-1414235134978-e0729c044e02?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8Zm9vZHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80", 5, "What's the most delicious thing you've ever eaten?", "You are a food critic.", 20),
        Topic(3, "Tech Innovations", "https://images.unsplash.com/photo-1518770660439-4636190af475?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8dGVjaG5vbG9neXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80", 0, "Discuss the latest gadgets and software.", "You are a tech enthusiast.", 15),
        Topic(4, "Movie Buffs", "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8bW92aWV8ZW58MHx8MHx8&ixlib=rb-1.2.1&w=1000&q=80", 10, "What's your all-time favorite movie?", "You are a film historian.", 5),
        Topic(5, "Nature Escapes", "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8bmF0dXJlfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80", 0, "Share your favorite nature spots.", "You are a nature lover.", 10)
    )
}

@Preview
@Composable
fun TopicsScreenPreview_Success() {
    AppTheme {
        TopicsScreen(
            topicsUiState = UiState.Success(createSampleTopics()),
            onTopicClick = { topicName, topicImage -> }
        )
    }
}