package com.foryouandme.core.data.api.feed.response

import arrow.core.computations.either
import com.foryouandme.core.data.api.common.response.activity.ActivityDataResponse
import com.foryouandme.core.data.api.common.response.activity.QuickActivityResponse
import com.foryouandme.core.data.api.common.response.activity.SurveyActivityResponse
import com.foryouandme.core.data.api.common.response.activity.TaskActivityResponse
import com.foryouandme.core.data.api.common.response.notifiable.FeedAlertResponse
import com.foryouandme.core.data.api.common.response.notifiable.FeedEducationalResponse
import com.foryouandme.core.data.api.common.response.notifiable.FeedRewardResponse
import com.foryouandme.core.data.api.common.response.notifiable.NotifiableDataResponse
import com.foryouandme.core.entity.feed.Feed
import com.foryouandme.core.entity.feed.FeedType
import com.foryouandme.core.ext.toEither
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.threeten.bp.ZonedDateTime

@JsonApi(type = "task")
data class FeedResponse(
    @field:Json(name = "from") val from: String? = null,
    @field:Json(name = "to") val to: String? = null,
    @field:Json(name = "rescheduled_times") val rescheduledTimes: Int? = null,
    @field:Json(name = "schedulable") val activity: HasOne<ActivityDataResponse>? = null,
    @field:Json(name = "notifiable") val feedNotification: HasOne<NotifiableDataResponse>? = null
) : Resource() {


    suspend fun toFeed(): Feed? =
        either.invoke<Unit, Feed> {

            Feed(
                id,
                !from.toEither().map { ZonedDateTime.parse(it) },
                !to.toEither().map { ZonedDateTime.parse(it) },
                !getFeedType().toEither()
            )

        }.orNull()

    private suspend fun getFeedType(): FeedType? =

        when {
            activity?.get(document) != null ->
                activity.get(document)
                    ?.let {
                        when (it) {
                            is QuickActivityResponse -> it.toQuickActivity(id)
                            is TaskActivityResponse ->
                                it.toTaskActivity(document, id, rescheduledTimes)
                            is SurveyActivityResponse ->
                                it.toTaskActivity(document, id, rescheduledTimes)
                            else -> null
                        }
                    }
                    ?.let { FeedType.StudyActivityFeed(it) }
            feedNotification?.get(document) != null ->
                feedNotification.get(document)
                    ?.let {
                        when (it) {
                            is FeedRewardResponse -> it.toFeedReward(id)
                            is FeedAlertResponse -> it.toFeedAlert(id)
                            is FeedEducationalResponse -> it.toFeedEducational(id)
                            else -> null
                        }
                    }
                    ?.let { FeedType.StudyNotifiableFeed(it) }
            else -> null
        }
}

suspend fun Array<FeedResponse>.toFeedItems(): List<Feed> = mapNotNull { it.toFeed() }
