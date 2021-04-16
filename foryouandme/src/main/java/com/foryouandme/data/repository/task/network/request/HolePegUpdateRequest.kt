package com.foryouandme.data.repository.task.network.request

import com.squareup.moshi.Json

data class HolePegUpdateRequest(
    @Json(name = "steps") val steps: List<HolePegStepUpdateRequest>,
    @Json(name = "start_time") val startTime: Long,
    @Json(name = "end_time") val endTime: Long
)

data class HolePegStepUpdateRequest(
    @Json(name = "pegs") val pegs: List<PegUpdateRequest>,
    @Json(name = "start_time") val startTime: Long,
    @Json(name = "end_time") val endTime: Long,
    @Json(name = "start_point") val startPoint: String,
    @Json(name = "target_point") val targetPoint: String,
    @Json(name = "number_of_errors") val numberOfErrors: Int,
    @Json(name = "number_of_pegs") val numberOfPegs: Int,
)

data class PegUpdateRequest(
    @Json(name = "start_time") val startTime: Long,
    @Json(name = "end_time") val endTime: Long,
    @Json(name = "distance") val distance: Double
)