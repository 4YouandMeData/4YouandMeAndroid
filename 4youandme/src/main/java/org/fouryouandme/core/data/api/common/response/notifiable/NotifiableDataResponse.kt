package org.fouryouandme.core.data.api.common.response.notifiable

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.notifiable.FeedAlert
import org.fouryouandme.core.entity.notifiable.FeedEducational
import org.fouryouandme.core.entity.notifiable.FeedReward
import org.fouryouandme.core.ext.decodeBase64ImageFx
import org.fouryouandme.core.ext.mapNotNull

open class NotifiableDataResponse(
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "rewardable_identifier") val rewardableIdentifier: String? = null,
    @field:Json(name = "link_url") val linkUrl: String? = null,
    @field:Json(name = "card_color") val cardColor: String? = null,
    @field:Json(name ="task_action_button_label") val taskActionButtonLabel: String? = null
) : Resource()

@JsonApi(type = "feed_reward")
class FeedRewardResponse(
    title: String? = null,
    description: String? = null,
    rewardableIdentifier: String? = null,
    linkUrl: String? = null,
    taskActionButtonLabel: String? = null,
    @field:Json(name = "image") val image: String? = null
) : NotifiableDataResponse(title, description, rewardableIdentifier, linkUrl, taskActionButtonLabel) {

    suspend fun toFeedReward(id: String): FeedReward =
        FeedReward(
            id,
            title,
            description,
            mapNotNull(cardColor, cardColor)?.let { HEXGradient(it.a, it.b) },
            image?.decodeBase64ImageFx()?.orNull(),
            linkUrl,
            taskActionButtonLabel
        )
}

@JsonApi(type = "feed_alert")
class FeedAlertResponse(
    title: String? = null,
    description: String? = null,
    alertIdentifier: String? = null,
    linkUrl: String? = null,
    taskActionButtonLabel: String? = null,
    @field:Json(name = "image") val image: String? = null
) : NotifiableDataResponse(title, description, alertIdentifier, linkUrl, taskActionButtonLabel) {

    suspend fun toFeedAlert(id: String): FeedAlert =
        FeedAlert(
            id,
            title,
            description,
            mapNotNull(cardColor, cardColor)?.let { HEXGradient(it.a, it.b) },
            image?.decodeBase64ImageFx()?.orNull(),
            linkUrl,
            taskActionButtonLabel
        )
}

@JsonApi(type = "feed_educational")
class FeedEducationalResponse(
    title: String? = null,
    description: String? = null,
    educationalIdentifier: String? = null,
    linkUrl: String? = null,
    taskActionButtonLabel: String? = null,
    @field:Json(name = "image") val image: String? = null
) : NotifiableDataResponse(title, description, educationalIdentifier, linkUrl, taskActionButtonLabel) {

    suspend fun toFeedEducational(id: String): FeedEducational =
        FeedEducational(
            id,
            title,
            description,
            mapNotNull(cardColor, cardColor)?.let { HEXGradient(it.a, it.b) },
            image?.decodeBase64ImageFx()?.orNull(),
            linkUrl,
            taskActionButtonLabel
        )
}