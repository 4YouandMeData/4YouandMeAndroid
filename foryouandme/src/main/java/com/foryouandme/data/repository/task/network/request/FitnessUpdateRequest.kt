package com.foryouandme.data.repository.task.network.request

import com.squareup.moshi.Json

data class FitnessUpdateRequest(
    @Json(name = "fitness_walk") val fitnessWalkRequest: FitnessWalkRequest,
    @Json(name = "fitness_rest") val fitnessSit: FitnessSitRequest,
)

data class FitnessWalkRequest(
    @Json(name = "device_motion_info") val deviceMotion: String,
    @Json(name = "accelerometer_info") val accelerometer: String,
    @Json(name = "pedometer_info") val pedometer: String,
)

data class FitnessSitRequest(
    @Json(name = "device_motion_info") val deviceMotion: String,
    @Json(name = "accelerometer_info") val accelerometer: String,
)