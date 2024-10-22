package org.ailingo.app.feature_dicitionary_predictor.data

import kotlinx.serialization.Serializable

@Serializable
data class PredictorRequest(
    val correctTypoInPartialWord: Boolean,
    val languages: List<String>,
    val maxNumberOfPredictions: Int,
    val text: String,
    val token: String
)