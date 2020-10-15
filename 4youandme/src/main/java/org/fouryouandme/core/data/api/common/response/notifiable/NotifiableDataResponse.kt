package org.fouryouandme.core.data.api.common.response.notifiable

import android.graphics.Bitmap
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.notifiable.FeedReward
import org.fouryouandme.core.ext.decodeBase64ImageFx
import org.fouryouandme.core.ext.mapNotNull

open class NotifiableDataResponse(
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "rewardable_identifier") val rewardableIdentifier: String? = null
) : Resource()

@JsonApi(type = "feed_reward")
class FeedRewardResponse(
    title: String? = null,
    description: String? = null,
    rewardableIdentifier: String? = null,
    @field:Json(name = "image") val image: String? = null
) : NotifiableDataResponse(title, description, rewardableIdentifier) {

    suspend fun toFeedReward(id: String): FeedReward =
        FeedReward(
            id,
            title,
            description,
            image?.decodeBase64ImageFx()?.orNull()
        )
}