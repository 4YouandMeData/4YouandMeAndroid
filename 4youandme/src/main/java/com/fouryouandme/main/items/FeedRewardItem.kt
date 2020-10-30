package com.fouryouandme.main.items

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.fouryouandme.R
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.configuration.HEXGradient
import com.fouryouandme.core.entity.configuration.button.button
import com.fouryouandme.core.entity.notifiable.FeedReward
import com.fouryouandme.core.ext.getOr
import com.fouryouandme.core.ext.html.setHtmlText
import com.fouryouandme.core.ext.startCoroutine
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.feed_reward_item.*
import org.threeten.bp.ZonedDateTime

data class FeedRewardItem(
    val configuration: Configuration,
    val data: FeedReward,
    val from: ZonedDateTime,
    val to: ZonedDateTime
) : DroidItem<Unit> {
    override fun areTheSame(other: DroidItem<Any>): Boolean =
        when (other) {
            is FeedRewardItem -> data.id == other.data.id
            else -> false
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        when (other) {
            is FeedRewardItem ->
                (data.id == other.data.id)
                    .or(data.image == other.data.image)
                    .or(data.title == other.data.title)
                    .or(data.description == other.data.description)
            else -> false
        }
}


class FeedRewardViewHolder(viewGroup: ViewGroup, val start: (FeedRewardItem) -> Unit) :
    DroidViewHolder<FeedRewardItem, Unit>(viewGroup, R.layout.feed_reward_item),
    LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(t: FeedRewardItem, position: Int) {

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

            link.isVisible = t.data.linkUrl.isNullOrEmpty().not()
            link.text =
                t.data.taskActionButtonLabel.getOr { t.configuration.text.feed.rewardButtonDefault }
            link.setTextColor(t.configuration.theme.primaryTextColor.color())
            link.background = button(t.configuration.theme.secondaryColor.color())
            link.setOnClickListener { start(t) }

        }

    }

    companion object {

        fun factory(start: (FeedRewardItem) -> Unit): ViewHolderFactory =
            ViewHolderFactory(
                { FeedRewardViewHolder(it, start) },
                { _, item -> item is FeedRewardItem }
            )

    }
}