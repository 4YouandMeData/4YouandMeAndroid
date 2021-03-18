package com.foryouandme.data.repository.feed.network.response

import com.foryouandme.core.data.api.common.response.activity.ActivityDataResponse
import com.foryouandme.core.data.api.common.response.activity.QuickActivityResponse
import com.foryouandme.core.data.api.common.response.activity.SurveyActivityResponse
import com.foryouandme.core.data.api.common.response.activity.TaskActivityResponse
import com.foryouandme.core.data.api.common.response.notifiable.FeedAlertResponse
import com.foryouandme.core.data.api.common.response.notifiable.FeedEducationalResponse
import com.foryouandme.core.data.api.common.response.notifiable.FeedRewardResponse
import com.foryouandme.core.data.api.common.response.notifiable.NotifiableDataResponse
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.entity.feed.Feed
import com.foryouandme.entity.feed.FeedType
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
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
    @field:Json(name = "notifiable") val feedNotification: HasOne<NotifiableDataResponse>? = null,
) : Resource() {


    fun toFeed(moshi: Moshi): Feed? {

        val fromDate = catchToNull { from?.let { ZonedDateTime.parse(it) } }
        val toDate = catchToNull { to?.let { ZonedDateTime.parse(it) } }
        val feedType = getFeedType(moshi)

        return when (null) {
            fromDate, toDate, feedType -> null
            else ->
                Feed(
                    id,
                    fromDate,
                    toDate,
                    feedType
                )
        }

    }

    private fun getFeedType(moshi: Moshi): FeedType? =

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
                            is FeedRewardResponse -> it.toFeedReward(id, meta, moshi)
                            is FeedAlertResponse -> it.toFeedAlert(id)
                            is FeedEducationalResponse -> it.toFeedEducational(id)
                            else -> null
                        }
                    }
                    ?.let { FeedType.StudyNotifiableFeed(it) }
            else -> null
        }
}

fun Array<FeedResponse>.toFeedItems(moshi: Moshi): List<Feed> = mapNotNull { it.toFeed(moshi) }
