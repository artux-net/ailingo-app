package org.ailingo.app.features.topics.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.topics.data.model.Topic

interface TopicRepository {
    fun getTopics(): Flow<UiState<List<Topic>>>
}