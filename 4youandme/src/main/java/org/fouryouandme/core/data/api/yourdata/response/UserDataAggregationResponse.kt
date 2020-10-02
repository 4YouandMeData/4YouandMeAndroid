package org.fouryouandme.core.data.api.yourdata.response

import com.squareup.moshi.Json

data class UserDataAggregationResponse(
    @Json(name = "data") val data: List<UserDataAggregationDataResponse>? = null
)

data class UserDataAggregationDataResponse(
    @Json(name = "id") val id: String? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "attributes") val attributes: UserDataAggregationAttributesResponse? = null
)

data class UserDataAggregationAttributesResponse(
    @Json(name = "title") val title: String? = null,
    @Json(name = "strategy") val strategy: String? = null,
    @Json(name = "color") val color: String? = null,
    @Json(name = "data") val data: UserDataAggregationAttributesDataResponse? = null
)

data class UserDataAggregationAttributesDataResponse(
    @Json(name = "data") val data: List<Int?>? = null,
    @Json(name = "x_labels") val xLabels: List<String>? = null,
    @Json(name = "y_labels") val yLabels: List<String>? = null
)