package org.fouryouandme.main.items

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import arrow.core.getOrElse
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.task_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.activity.TaskActivity
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.decodeBase64Image
import org.fouryouandme.core.ext.getOrEmpty
import org.threeten.bp.ZonedDateTime

data class TaskActivityItem(
    val configuration: Configuration,
    val data: TaskActivity,
    val from: ZonedDateTime,
    val to: ZonedDateTime
) : DroidItem {

    override fun areTheSame(other: DroidItem): Boolean =
        when (other) {
            is TaskActivityItem -> data.id == other.data.id
            else -> false
        }

    override fun haveTheSameContent(other: DroidItem): Boolean =
        when (other) {
            is TaskActivityItem ->
                (data.id == other.data.id)
                    .or(data.image == other.data.image)
                    .or(data.title == other.data.title)
                    .or(data.description == other.data.description)
                    .or(data.button == other.data.button)
                    .or(data.gradient == other.data.gradient)
                    .or(data.activityType == data.activityType)
            else -> false
        }

    override fun getPayload(other: DroidItem): List<*> = emptyList<Unit>()

}

class TaskActivityViewHolder(viewGroup: ViewGroup) :
    DroidViewHolder<TaskActivityItem, Unit>(viewGroup, R.layout.task_item), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(t: TaskActivityItem, position: Int) {

        card_content.background =
            t.data.gradient
                .getOrElse {
                    HEXGradient.from(
                        t.configuration.theme.primaryColorStart,
                        t.configuration.theme.primaryColorEnd
                    )
                }.drawable()

        val bitmap = t.data.image.flatMap { it.decodeBase64Image() }
        bitmap.map { image.setImageBitmap(it) }
        image.isVisible = bitmap.isDefined()

        title.text = t.data.title.getOrEmpty()
        title.isVisible = t.data.title.isDefined()
        title.setTextColor(t.configuration.theme.secondaryColor.color())

        body.text = t.data.description.getOrEmpty()
        body.isVisible = t.data.description.isDefined()
        body.setTextColor(t.configuration.theme.secondaryColor.color())

        button.isVisible = t.data.activityType.isDefined()
        button.text =
            t.data.button.getOrElse { t.configuration.text.activity.activityButtonDefault }
        button.setTextColor(t.configuration.theme.primaryTextColor.color())
        button.background = button(t.configuration.theme.secondaryColor.color())

    }

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { TaskActivityViewHolder(it) },
                { _, item -> item is TaskActivityItem }
            )

    }

}