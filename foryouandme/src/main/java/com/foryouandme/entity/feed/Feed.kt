package com.foryouandme.entity.feed

import com.foryouandme.entity.activity.StudyActivity
import com.foryouandme.entity.activity.TaskActivity
import com.foryouandme.entity.mock.Mock
import com.foryouandme.entity.notifiable.StudyNotifiable
import org.threeten.bp.ZonedDateTime

data class Feed(
    val id: String,
    val from: ZonedDateTime,
    val to: ZonedDateTime,
    val type: FeedType
) {

    companion object {

        fun mock(): Feed =
            Feed(
                id = "id",
                from = Mock.zoneDateTime,
                to = Mock.zoneDateTime,
                type = FeedType.StudyActivityFeed(TaskActivity.mock())
            )

    }

}

sealed class FeedType {

    data class StudyActivityFeed(val studyActivity: StudyActivity) : FeedType()
    data class StudyNotifiableFeed(val studyNotifiable: StudyNotifiable) : FeedType()

}