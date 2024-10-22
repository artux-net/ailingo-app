package org.ailingo.app.feature_dicitionary_predictor.data

import kotlinx.serialization.Serializable

@Serializable
data class Prediction(
    val completionStartingIndex: Int,
    val model_unique_identifier: String,
    val score: Float,
    val scoreBeforeRescoring: Float,
    val source: String,
    val text: String
)