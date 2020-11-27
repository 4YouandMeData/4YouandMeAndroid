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
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
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

    override fun bind(item: TaskActivityItem, position: Int) {

        startCoroutine {

            card_content.background =
                item.data.gradient
                    .getOr {
                        HEXGradient.from(
                            item.configuration.theme.primaryColorStart,
                            item.configuration.theme.primaryColorEnd
                        )
                    }.drawable()

            item.data.image?.let { image.setImageBitmap(it) }
            image.isVisible = item.data.image != null

            title.setHtmlText(item.data.title.orEmpty(), true)
            title.isVisible = item.data.title != null
            title.setTextColor(item.configuration.theme.secondaryColor.color())

            body.setHtmlText(item.data.description.orEmpty(), true)
            body.isVisible = item.data.description != null
            body.setTextColor(item.configuration.theme.secondaryColor.color())

            review.isVisible = item.data.activityType != null
            review.text =
                item.data.button.getOr { item.configuration.text.activity.activityButtonDefault }
            review.setTextColor(item.configuration.theme.primaryTextColor.color())
            review.background = button(item.configuration.theme.secondaryColor.color())
            review.setOnClickListener { start(item) }

        }

    }

    companion object {

        fun factory(start: (TaskActivityItem) -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { TaskActivityViewHolder(it, start) },
                { _, item -> item is TaskActivityItem }
            )

    }

}