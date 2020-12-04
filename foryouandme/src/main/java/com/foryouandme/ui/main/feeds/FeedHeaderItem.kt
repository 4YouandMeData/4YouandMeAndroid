package com.foryouandme.ui.main.feeds

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.core.entity.configuration.Configuration
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.feed_header_item.*

data class FeedHeaderItem(
    val configuration: Configuration,
    val id: String,
    val points: String?,
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<FeedHeaderItem> {
            it.id == id
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<FeedHeaderItem> {
            it == this
        }
}

class FeedHeaderViewHolder(parent: ViewGroup) :
    DroidViewHolder<FeedHeaderItem, Unit>(parent, R.layout.feed_header_item), LayoutContainer {
    override fun bind(item: FeedHeaderItem, position: Int) {

        root.setBackgroundColor(item.configuration.theme.primaryColorEnd.color())

        header.setTextColor(item.configuration.theme.secondaryColor.color())
        header.text = item.configuration.text.tab.feedHeaderTitle
        header.alpha = 0.5f

        title.setTextColor(item.configuration.theme.secondaryTextColor.color())
        title.text = item.configuration.text.tab.feedHeaderSubTitle

        points_counter.setTextColor(item.configuration.theme.secondaryTextColor.color())
        points_counter.text = item.points
        points_counter.isVisible = item.points != null

        points_label.setTextColor(item.configuration.theme.secondaryColor.color())
        points_label.text = item.configuration.text.tab.feedHeaderPoints
        points_label.alpha = 0.5f
        points_label.isVisible = item.points != null

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { FeedHeaderViewHolder(it) },
                { _, item -> item is FeedHeaderItem }
            )

    }
}
