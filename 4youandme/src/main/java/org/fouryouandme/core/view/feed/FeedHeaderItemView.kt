package org.fouryouandme.core.view.feed

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.feed_header_item.view.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration

class FeedHeaderItemView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.feed_header_item, this)

    }

    fun applyData(
        configuration: Configuration,
        pointsRes: String
    ): Unit {
        header.setTextColor(configuration.theme.secondaryTextColor.color()) // TODO: set alpha to 0.6
        header.text = "TODAY"

        title.setTextColor(configuration.theme.secondaryTextColor.color())
        title.text = "Pregnancy Overview"

        points_counter.setTextColor(configuration.theme.secondaryTextColor.color())
        points_counter.text = pointsRes

        points_label.setTextColor(configuration.theme.secondaryTextColor.color()) // TODO: set alpha to 0.6
        points_label.text = "points"

    }

}