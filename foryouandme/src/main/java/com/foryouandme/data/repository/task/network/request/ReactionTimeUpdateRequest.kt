package com.foryouandme.data.repository.task.network.request

import com.squareup.moshi.Json

data class ReactionTimeUpdateRequest(
    @Json(name = "attempts") val attempts: List<ReactionTimeAttemptRequest>
)

data class ReactionTimeAttemptRequest(
    @Json(name = "device_motion_info") val deviceInfoMotion: String,
    @Json(name = "timestamp") val timestamp: Long
)