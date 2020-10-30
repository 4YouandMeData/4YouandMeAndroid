package com.foryouandme.main.items

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.core.entity.activity.TaskActivity
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.HEXGradient
import com.foryouandme.core.entity.configuration.button.button
import com.foryouandme.core.ext.getOr
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.startCoroutine
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.task_activity_item.*
import org.threeten.bp.ZonedDateTime

data class TaskActivityItem(
    val configuration: Configuration,
    val data: TaskActivity,
    val from: ZonedDateTime,
    val to: ZonedDateTime
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        when (other) {
            is TaskActivityItem -> data.taskId == other.data.taskId
            else -> false
        }

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        when (other) {
            is TaskActivityItem ->
                (data.taskId == other.data.taskId)
                    .or(data.image == other.data.image)
                    .or(data.title == other.data.title)
                    .or(data.description == other.data.description)
                    .or(data.button == other.data.button)
                    .or(data.gradient == other.data.gradient)
                    .or(data.activityType == data.activityType)
            else -> false
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

}

class TaskActivityViewHolder(viewGroup: ViewGroup, val start: (TaskActivityItem) -> Unit) :
    DroidViewHolder<TaskActivityItem, Unit>(viewGroup, R.layout.task_activity_item),
    LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(t: TaskActivityItem, position: Int) {

        startCoroutine {

            card_content.background =
                t.data.gradient
                    .getOr {
                        HEXGradient.from(
                            t.configuration.theme.primaryColorStart,
                            t.configuration.theme.primaryColorEnd
                        )
                    }.drawable()

            t.data.image?.let { image.setImageBitmap(it) }
            image.isVisible = t.data.image != null

            title.setHtmlText(t.data.title.orEmpty(), true)
            title.isVisible = t.data.title != null
            title.setTextColor(t.configuration.theme.secondaryColor.color())

            body.setHtmlText(t.data.description.orEmpty(), true)
            body.isVisible = t.data.description != null
            body.setTextColor(t.configuration.theme.secondaryColor.color())

            review.isVisible = t.data.activityType != null
            review.text =
                t.data.button.getOr { t.configuration.text.activity.activityButtonDefault }
            review.setTextColor(t.configuration.theme.primaryTextColor.color())
            review.background = button(t.configuration.theme.secondaryColor.color())
            review.setOnClickListener { start(t) }

        }

    }

    companion object {

        fun factory(start: (TaskActivityItem) -> Unit): ViewHolderFactory =
            ViewHolderFactory(
                { TaskActivityViewHolder(it, start) },
                { _, item -> item is TaskActivityItem }
            )

    }

}