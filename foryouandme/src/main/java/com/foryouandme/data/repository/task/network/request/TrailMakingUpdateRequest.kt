package com.foryouandme.data.repository.task.network.request

import com.squareup.moshi.Json

data class TrailMakingUpdateRequest(
    @Json(name = "start_time") val startTime: Long,
    @Json(name = "numberOfErrors") val numberOfErrors: Int,
    @Json(name = "attempts") val taps: List<TrailMakingTapUpdateRequest>,
    @Json(name = "end_time") val endTime: Long
)

data class TrailMakingTapUpdateRequest(
    @Json(name = "index") val index: Int,
    @Json(name = "timestamp") val timestamp: Long,
    @Json(name = "incorrect") val incorrect: Boolean
)