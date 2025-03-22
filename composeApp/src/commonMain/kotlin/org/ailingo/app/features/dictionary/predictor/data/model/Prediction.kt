package org.ailingo.app.features.dictionary.predictor.data.model

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
