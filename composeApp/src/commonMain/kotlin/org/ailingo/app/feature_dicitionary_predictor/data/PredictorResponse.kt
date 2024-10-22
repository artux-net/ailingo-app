package org.ailingo.app.feature_dicitionary_predictor.data

import kotlinx.serialization.Serializable

@Serializable
data class PredictorResponse(
    val language: String,
    val predictions: List<Prediction>,
    val text: String
)