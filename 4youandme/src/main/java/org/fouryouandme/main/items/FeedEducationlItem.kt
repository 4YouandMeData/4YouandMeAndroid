package org.fouryouandme.main.items

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.feed_educationl_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.notifiable.FeedEducational
import org.fouryouandme.core.ext.getOr
import org.fouryouandme.core.ext.html.setHtmlText
import org.fouryouandme.core.ext.startCoroutine
import org.threeten.bp.ZonedDateTime

data class FeedEducationalItem(
    val configuration: Configuration,
    val data: FeedEducational,
    val from: ZonedDateTime,
    val to: ZonedDateTime
) : DroidItem<Unit> {
    override fun areTheSame(other: DroidItem<Any>): Boolean =
        when (other) {
            is FeedEducationalItem -> data.id == other.data.id
            else -> false
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        when (other) {
            is FeedEducationalItem ->
                (data.id == other.data.id)
                    .or(data.image == other.data.image)
                    .or(data.title == other.data.title)
                    .or(data.description == other.data.description)
            else -> false
        }
}


class FeedEducationalViewHolder(viewGroup: ViewGroup, val start: (FeedEducationalItem) -> Unit) :
    DroidViewHolder<FeedEducationalItem, Unit>(viewGroup, R.layout.feed_educationl_item),
    LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(t: FeedEducationalItem, position: Int) {

        startCoroutine {

            card_content.background =
                HEXGradient.from(
                    t.configuration.theme.primaryColorStart,
                    t.configuration.theme.primaryColorEnd
                ).drawable()

            t.data.image?.let { image.setImageBitmap(it) }
            image.isVisible = t.data.image != null

            title.setHtmlText(t.data.title.orEmpty(), true)
            title.isVisible = t.data.title != null
            title.setTextColor(t.configuration.theme.secondaryColor.color())

            body.setHtmlText(t.data.description.orEmpty(), true)
            body.isVisible = t.data.description != null
            body.setTextColor(t.configuration.theme.secondaryColor.color())

            link.isVisible = t.data.linkUrl.isNullOrEmpty().not()
            link.text =
                t.data.taskActionButtonLabel.getOr { t.configuration.text.feed.educationalButtonDefault }
            link.setTextColor(t.configuration.theme.primaryTextColor.color())
            link.background = button(t.configuration.theme.secondaryColor.color())
            link.setOnClickListener { start(t) }

        }

    }

    companion object {

        fun factory(start: (FeedEducationalItem) -> Unit): ViewHolderFactory =
            ViewHolderFactory(
                { FeedEducationalViewHolder(it, start) },
                { _, item -> item is FeedEducationalItem }
            )

    }
}