package com.foryouandme.ui.main.compose

import com.foryouandme.entity.activity.QuickActivity
import com.foryouandme.entity.activity.TaskActivity
import com.foryouandme.entity.mock.Mock
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

sealed class FeedItem {

    data class TaskActivityItem(
        val data: TaskActivity,
        val from: ZonedDateTime,
        val to: ZonedDateTime
    ) : FeedItem() {

        companion object {

            fun mock(): TaskActivityItem =
                TaskActivityItem(
                    data = TaskActivity.mock(),
                    from = Mock.zoneDateTime,
                    to = Mock.zoneDateTime
                )

        }

    }

    data class QuickActivitiesItem(
        val items: List<QuickActivityItem>,
        val selectedIndex: Int
    ) : FeedItem() {

        companion object {

            fun mock(): QuickActivitiesItem =
                QuickActivitiesItem(
                    items = listOf(QuickActivityItem.mock(), QuickActivityItem.mock()),
                    selectedIndex = 0
                )

        }

    }

    data class QuickActivityItem(
        val data: QuickActivity,
        val selectedAnswer: String?,
    ) : FeedItem() {

        companion object {

            fun mock(): QuickActivityItem =
                QuickActivityItem(
                    data = QuickActivity.mock(),
                    selectedAnswer = null
                )

        }

    }

    data class DateItem(val date: LocalDate) : FeedItem() {

        companion object {

            fun mock(): DateItem =
                DateItem(date = Mock.date)

        }

    }

}