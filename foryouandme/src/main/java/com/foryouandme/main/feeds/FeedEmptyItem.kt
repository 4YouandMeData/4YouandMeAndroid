package com.foryouandme.main.feeds

import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.core.entity.configuration.Configuration
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.feed_empty_item.*

data class FeedEmptyItem(val configuration: Configuration, val id: String = "0") : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<FeedEmptyItem> { it.id == id }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<FeedEmptyItem> { it == this }
}


class FeedEmptyViewHolder(
    viewGroup: ViewGroup
) : DroidViewHolder<FeedEmptyItem, Unit>(viewGroup, R.layout.feed_empty_item), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(item: FeedEmptyItem, position: Int) {

        empty_title.setTextColor(item.configuration.theme.primaryTextColor.color())
        empty_title.text = item.configuration.text.tab.feedEmptyTitle

        empty_description.setTextColor(item.configuration.theme.primaryTextColor.color())
        empty_description.text = item.configuration.text.tab.feedEmptySubTitle

    }

    companion object {

        fun factory(): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { FeedEmptyViewHolder(it) },
                { _, item -> item is FeedEmptyItem }
            )

    }
}