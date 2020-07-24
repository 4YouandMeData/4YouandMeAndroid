package org.fouryouandme.main.tasks

import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.task_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.decodeBase64Image
import org.threeten.bp.ZonedDateTime

data class TaskItem(
    val id: String,
    val configuration: Configuration,
    val image: String,
    val title: String,
    val body: String,
    val button: String,
    val background: HEXGradient,
    val date: ZonedDateTime
) : DroidItem {

    override fun areTheSame(other: DroidItem): Boolean =
        when (other) {
            is TaskItem -> id == other.id
            else -> false
        }

    override fun haveTheSameContent(other: DroidItem): Boolean =
        when (other) {
            is TaskItem ->
                (id == other.id)
                    .or(image == other.image)
                    .or(title == other.title)
                    .or(body == other.body)
                    .or(button == other.button)
                    .or(background == other.background)
            else -> false
        }

    override fun getPayload(other: DroidItem): List<*> = emptyList<Unit>()

}

class TaskViewHolder(viewGroup: ViewGroup) :
    DroidViewHolder<TaskItem, Unit>(viewGroup, R.layout.task_item), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(t: TaskItem, position: Int) {

        card_content.background = t.background.drawable()

        image.setImageBitmap(t.image.decodeBase64Image().orNull())

        title.text = t.title
        title.setTextColor(t.configuration.theme.secondaryColor.color())

        body.text = t.body
        body.setTextColor(t.configuration.theme.secondaryColor.color())

        button.text = t.button
        button.setTextColor(t.configuration.theme.primaryTextColor.color())
        button.background = button(t.configuration.theme.secondaryColor.color())

    }


}