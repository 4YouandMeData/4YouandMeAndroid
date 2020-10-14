package org.fouryouandme.main.feeds

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.feed_header_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration

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
    override fun bind(t: FeedHeaderItem, position: Int) {

        root.setBackgroundColor(t.configuration.theme.primaryColorEnd.color())

        header.setTextColor(t.configuration.theme.secondaryColor.color())
        header.text = t.configuration.text.tab.feedHeaderTitle
        header.alpha = 0.5f

        title.setTextColor(t.configuration.theme.secondaryTextColor.color())
        title.text = t.configuration.text.tab.feedHeaderSubTitle

        points_counter.setTextColor(t.configuration.theme.secondaryTextColor.color())
        points_counter.text = t.points
        points_counter.isVisible = t.points != null

        points_label.setTextColor(t.configuration.theme.secondaryColor.color())
        points_label.text = t.configuration.text.tab.feedHeaderPoints
        points_label.alpha = 0.5f
        points_label.isVisible = t.points != null

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { FeedHeaderViewHolder(it) },
                { _, item -> item is FeedHeaderItem }
            )

    }
}
