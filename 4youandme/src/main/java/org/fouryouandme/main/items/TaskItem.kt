package org.fouryouandme.main.items

import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.task_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.task.Task
import org.fouryouandme.core.ext.decodeBase64Image

data class TaskItem(
    val configuration: Configuration,
    val data: Task
) : DroidItem {

    override fun areTheSame(other: DroidItem): Boolean =
        when (other) {
            is TaskItem -> data.id == other.data.id
            else -> false
        }

    override fun haveTheSameContent(other: DroidItem): Boolean =
        when (other) {
            is TaskItem ->
                (data.id == other.data.id)
                    .or(data.image == other.data.image)
                    .or(data.title == other.data.title)
                    .or(data.body == other.data.body)
                    .or(data.button == other.data.button)
                    .or(data.background == other.data.background)
            else -> false
        }

    override fun getPayload(other: DroidItem): List<*> = emptyList<Unit>()

}

fun Task.toItem(configuration: Configuration): TaskItem =
    TaskItem(configuration, this)

fun List<Task>.toItems(configuration: Configuration): List<TaskItem> =
    map { it.toItem(configuration) }

class TaskViewHolder(viewGroup: ViewGroup) :
    DroidViewHolder<TaskItem, Unit>(viewGroup, R.layout.task_item), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(t: TaskItem, position: Int) {

        card_content.background = t.data.background.drawable()

        image.setImageBitmap(t.data.image.decodeBase64Image().orNull())

        title.text = t.data.title
        title.setTextColor(t.configuration.theme.secondaryColor.color())

        body.text = t.data.body
        body.setTextColor(t.configuration.theme.secondaryColor.color())

        button.text = t.data.button
        button.setTextColor(t.configuration.theme.primaryTextColor.color())
        button.background = button(t.configuration.theme.secondaryColor.color())

    }

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { TaskViewHolder(it) },
                { _, item -> item is TaskItem }
            )

    }

}