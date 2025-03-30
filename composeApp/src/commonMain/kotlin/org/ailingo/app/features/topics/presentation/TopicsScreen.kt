package org.ailingo.app.features.topics.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.topic_list_empty
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ailingo.app.core.presentation.EmptyScreen
import org.ailingo.app.core.presentation.ErrorScreen
import org.ailingo.app.core.presentation.LoadingScreen
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.core.utils.windowinfo.info.WindowInfo
import org.ailingo.app.core.utils.windowinfo.util.DeviceType
import org.ailingo.app.features.topics.data.model.Topic

@Composable
fun TopicsScreen(
    windowInfo: WindowInfo,
    topicsUiState: UiState<List<Topic>>,
    onTopicClick: (String, String)->Unit
) {
    val deviceType = if (windowInfo.screenWidthInfo is WindowInfo.WindowType.DesktopWindowInfo) {
        DeviceType.Desktop
    } else {
        DeviceType.Mobile
    }

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
                TopicsContent(topicsUiState.data, deviceType, onTopicClick)
            }
        }
    }
}

@Composable
fun TopicsContent(
    topics: List<Topic>,
    deviceType: DeviceType,
    onTopicClick: (String, String) -> Unit
) {
    if (deviceType == DeviceType.Desktop) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(350.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(end = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(topics) { photo ->
                ContentTopics(photo, deviceType = deviceType, onTopicClick)
            }
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(150.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(topics) { photo ->
                ContentTopics(photo, deviceType = deviceType, onTopicClick)
            }
        }
    }
}