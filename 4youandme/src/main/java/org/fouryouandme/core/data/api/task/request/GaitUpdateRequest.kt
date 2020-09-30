package org.fouryouandme.core.data.api.task.request

import com.squareup.moshi.Json

data class GaitUpdateRequest(
    @Json(name = "gait_outbound") val gaitOutbound: GaitOutboundRequest,
    @Json(name = "gait_return") val gaitReturn: GaitReturnRequest,
    @Json(name = "gait_rest") val gaitRest: GaitRestRequest,
    @Json(name = "start_time") val startTime: Double,
    @Json(name = "end_time") val endTime: Double
)

data class GaitOutboundRequest(
    @Json(name = "device_motion_info") val deviceMotion: String,
    @Json(name = "accelerometer_info") val accelerometer: String,
    @Json(name = "pedometer_info") val pedometer: String,
)

data class GaitReturnRequest(
    @Json(name = "device_motion_info") val deviceMotion: String,
    @Json(name = "accelerometer_info") val accelerometer: String,
    @Json(name = "pedometer_info") val pedometer: String,
)

data class GaitRestRequest(
    @Json(name = "device_motion_info") val deviceMotion: String,
    @Json(name = "accelerometer_info") val accelerometer: String,
)