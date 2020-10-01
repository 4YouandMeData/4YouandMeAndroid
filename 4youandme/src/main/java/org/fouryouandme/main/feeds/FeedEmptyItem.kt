package org.fouryouandme.main.feeds

import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.feed_empty_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration

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

    override fun bind(t: FeedEmptyItem, position: Int) {

        empty_title.setTextColor(t.configuration.theme.primaryTextColor.color())
        empty_title.text = t.configuration.text.tab.feedEmptyTitle

        empty_description.setTextColor(t.configuration.theme.primaryTextColor.color())
        empty_description.text = t.configuration.text.tab.feedEmptySubTitle

    }

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { FeedEmptyViewHolder(it) },
                { _, item -> item is FeedEmptyItem }
            )

    }
}