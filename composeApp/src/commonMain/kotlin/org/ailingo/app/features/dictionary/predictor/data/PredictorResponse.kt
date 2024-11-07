package org.ailingo.app.features.dictionary.predictor.data

import kotlinx.serialization.Serializable

@Serializable
data class PredictorResponse(
    val language: String,
    val predictions: List<Prediction>,
    val text: String
)
