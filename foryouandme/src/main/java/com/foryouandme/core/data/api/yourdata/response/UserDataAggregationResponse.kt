package com.foryouandme.core.data.api.yourdata.response

import arrow.core.Either
import com.foryouandme.entity.yourdata.UserDataAggregation
import com.squareup.moshi.Json

data class UserDataAggregationResponse(
    @Json(name = "data") val data: List<UserDataAggregationDataResponse>? = null
) {

    suspend fun toUserAggregations(): List<UserDataAggregation>? =
        data?.mapNotNull {

            Either.catch {
                UserDataAggregation(
                    id = it.id!!,
                    type = it.type!!,
                    title = it.attributes?.title!!,
                    color = it.attributes.color,
                    data = it.attributes.data?.data?.map { if(it != null) it -1 else it }!!,
                    xLabels = it.attributes.data.xLabels!!,
                    yLabels = it.attributes.data.yLabels!!
                )
            }.orNull()

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