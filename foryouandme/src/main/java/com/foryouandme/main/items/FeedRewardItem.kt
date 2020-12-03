package com.foryouandme.main.items

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.HEXGradient
import com.foryouandme.core.entity.configuration.button.button
import com.foryouandme.core.entity.notifiable.FeedReward
import com.foryouandme.core.ext.getOr
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.startCoroutine
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
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

    override fun bind(item: FeedRewardItem, position: Int) {

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

            link.isVisible = item.data.action != null
            link.text =
                item.data.taskActionButtonLabel.getOr { item.configuration.text.feed.rewardButtonDefault }
            link.setTextColor(item.configuration.theme.primaryTextColor.color())
            link.background = button(item.configuration.theme.secondaryColor.color())
            link.setOnClickListener { start(item) }

        }

    }

    companion object {

        fun factory(start: (FeedRewardItem) -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { FeedRewardViewHolder(it, start) },
                { _, item -> item is FeedRewardItem }
            )

    }
}