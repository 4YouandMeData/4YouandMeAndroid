package com.foryouandme.data.repository.yourdata.network.response

import com.foryouandme.entity.yourdata.UserDataAggregation
import com.squareup.moshi.Json

data class UserDataAggregationResponse(
    @Json(name = "data") val data: List<UserDataAggregationDataResponse>? = null
) {

    fun toUserAggregations(): List<UserDataAggregation>? =
        data?.mapNotNull { response ->

            val id = response.id
            val type = response.type
            val strategy = response.attributes?.strategy
            val title = response.attributes?.title
            val color = response.attributes?.color
            val data = response.attributes?.data?.data?.map { if (it != null) it - 1 else it }
            val xLabels = response.attributes?.data?.xLabels
            val yLabels = response.attributes?.data?.yLabels

            when (null) {
                id, type, strategy, title, data, xLabels, yLabels -> null
                else ->
                    UserDataAggregation(
                        id = id,
                        type = type,
                        strategy = strategy,
                        title = title,
                        color = color,
                        data = data,
                        xLabels = xLabels,
                        yLabels = yLabels
                    )
            }
        }

}

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
    @Json(name = "data") val data: List<Float?>? = null,
    @Json(name = "x_labels") val xLabels: List<String>? = null,
    @Json(name = "y_labels") val yLabels: List<String>? = null
)