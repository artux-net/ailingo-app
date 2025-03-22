package org.ailingo.app.features.topics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.topics.data.model.Topic
import org.ailingo.app.features.topics.domain.repository.TopicRepository

class TopicViewModel(
    private val topicRepository: TopicRepository
) : ViewModel() {
    private val _topicState = MutableStateFlow<UiState<List<Topic>>>(UiState.Idle())
    val topicState = _topicState.asStateFlow()

    init {
        getTopics()
    }

    private fun getTopics() {
        viewModelScope.launch {
            topicRepository.getTopics().collect { state ->
                _topicState.update { state }
            }
        }
    }
}