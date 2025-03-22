package org.ailingo.app.features.dictionary.predictor.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorRequest
import org.ailingo.app.features.dictionary.predictor.data.model.PredictorResponse

interface PredictWordsRepository {
    fun predictNextWords(request: PredictorRequest): Flow<UiState<PredictorResponse>>
}