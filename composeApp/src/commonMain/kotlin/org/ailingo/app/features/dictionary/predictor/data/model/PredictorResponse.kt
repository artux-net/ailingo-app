package org.ailingo.app.features.dictionary.predictor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PredictorResponse(
    val language: String,
    val predictions: List<Prediction>,
    val text: String
)
