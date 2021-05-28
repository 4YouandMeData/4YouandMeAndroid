package com.foryouandme.data.repository.auth.answer.network.response.notifiable

import com.foryouandme.data.repository.auth.answer.network.response.meta.MetaResponse
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.core.ext.decodeBase64Image
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.integration.IntegrationApp
import com.foryouandme.entity.notifiable.FeedAction
import com.foryouandme.entity.notifiable.FeedAlert
import com.foryouandme.entity.notifiable.FeedEducational
import com.foryouandme.entity.notifiable.FeedReward
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.JsonBuffer
import moe.banana.jsonapi2.Resource

open class NotifiableDataResponse(
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "rewardable_identifier") val rewardableIdentifier: String? = null,
    @field:Json(name = "link_url") val linkUrl: String? = null,
    @field:Json(name = "card_color") val cardColor: String? = null,
    @field:Json(name = "task_action_button_label") val taskActionButtonLabel: String? = null
) : Resource()

@JsonApi(type = "feed_reward")
class FeedRewardResponse(
    title: String? = null,
    description: String? = null,
    rewardableIdentifier: String? = null,
    linkUrl: String? = null,
    taskActionButtonLabel: String? = null,
    @field:Json(name = "image") val image: String? = null
) : NotifiableDataResponse(
    title,
    description,
    rewardableIdentifier,
    linkUrl,
    taskActionButtonLabel
) {

    fun toFeedReward(id: String, meta: JsonBuffer<Any>?, moshi: Moshi): FeedReward {

        val adapter = moshi.adapter(MetaResponse::class.java)
        val metadata = catchToNull { meta?.get(adapter) }

        return FeedReward(
            id,
            if (title != null && metadata != null) metadata.applyMeta(title) else title,
            description,
            if (cardColor != null) HEXGradient(cardColor, cardColor) else null,
            image?.decodeBase64Image(),
            linkUrl?.toFeedAction(),
            taskActionButtonLabel
        )

    }

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

    fun toFeedAlert(id: String): FeedAlert =
        FeedAlert(
            id,
            title,
            description,
            if (cardColor != null) HEXGradient(cardColor, cardColor) else null,
            image?.decodeBase64Image(),
            linkUrl?.toFeedAction(),
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
) : NotifiableDataResponse(
    title,
    description,
    educationalIdentifier,
    linkUrl,
    taskActionButtonLabel
) {

    fun toFeedEducational(id: String): FeedEducational =
        FeedEducational(
            id,
            title,
            description,
            if (cardColor != null) HEXGradient(cardColor, cardColor) else null,
            image?.decodeBase64Image(),
            linkUrl?.toFeedAction(),
            taskActionButtonLabel
        )
}

private fun String.toFeedAction(): FeedAction? {

    val integration = IntegrationApp.fromIdentifier(this)

    return when {
        this == "feed" -> FeedAction.Feed
        this == "task" -> FeedAction.Tasks
        this == "your_data" -> FeedAction.YourData
        this == "study_info" -> FeedAction.StudyInfo
        this == "about_you" -> FeedAction.AboutYou
        this == "faq" -> FeedAction.Faq
        this == "rewards" -> FeedAction.Rewards
        this == "contacts" -> FeedAction.Contacts
        integration != null -> FeedAction.Integration(integration)
        isNotEmpty() -> FeedAction.Web(this)
        else -> null
    }

}