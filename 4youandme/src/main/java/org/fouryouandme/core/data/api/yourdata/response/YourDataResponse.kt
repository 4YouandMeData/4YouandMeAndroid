package org.fouryouandme.core.data.api.yourdata.response

import com.squareup.moshi.Json
import org.fouryouandme.core.entity.yourdata.YourData

data class YourDataResponse(@Json(name = "data") val data: YourDataInfoResponse? = null) {

    suspend fun toYourData(): YourData? =
        data?.id?.let {
            YourData(
                it,
                data.attributes?.title,
                data.attributes?.body,
                data.attributes?.stars
            )
        }
}

data class YourDataInfoResponse(
    @Json(name = "id") val id: String? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "attributes") val attributes: YourDataAttributesResponse? = null,
)

data class YourDataAttributesResponse(
    @Json(name = "title") val title: String? = null,
    @Json(name = "body") val body: String? = null,
    @Json(name = "stars") val stars: Int? = null,
)