package org.ailingo.app.features.dictionary.predictor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PredictorRequest(
    val correctTypoInPartialWord: Boolean,
    val languages: List<String>,
    val maxNumberOfPredictions: Int,
    val text: String,
    val token: String
)
