package org.fouryouandme.core.entity.feed

import org.fouryouandme.core.entity.activity.StudyActivity
import org.fouryouandme.core.entity.notifiable.StudyNotifiable
import org.threeten.bp.ZonedDateTime

data class Feed(
    val id: String,
    val from: ZonedDateTime,
    val to: ZonedDateTime,
    val type: FeedType
)

sealed class FeedType {

    data class StudyActivityFeed(val studyActivity: StudyActivity) : FeedType()
    data class StudyNotifiableFeed(val studyNotifiable: StudyNotifiable) : FeedType()

}