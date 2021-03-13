package com.foryouandme.data.repository.task.network.request

import com.squareup.moshi.Json

data class ReactionTimeUpdateRequest(
    @Json(name = "start_time") val startTime: Long,
    @Json(name = "attempts") val attempts: List<ReactionTimeAttemptRequest>,
    @Json(name = "end_time") val endTime: Long
)

data class ReactionTimeAttemptRequest(
    @Json(name = "device_motion_info") val deviceInfoMotion: String,
    @Json(name = "timestamp") val timestamp: Long
)