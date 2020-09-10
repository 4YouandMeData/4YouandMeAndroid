package org.fouryouandme.researchkit.step.introduction

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.step_introduction_header_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration

data class IntroductionHeaderItem(
    val title: String,
    @DrawableRes val image: Int,
    val configuration: Configuration
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        compare<IntroductionHeaderItem> {
            it.title == title &&
                    it.image == image
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        compare<IntroductionHeaderItem> {
            it.title == title &&
                    it.image == image
        }

}

class IntroductionHeaderViewHolder(
    parent: ViewGroup
) : DroidViewHolder<IntroductionHeaderItem, Unit>(
    parent,
    { layoutInflater, viewGroup, b ->
        layoutInflater.inflate(
            R.layout.step_introduction_header_item,
            viewGroup,
            b
        )
    }
), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(t: IntroductionHeaderItem, position: Int) {

        title.text = t.title
        title.setTextColor(t.configuration.theme.fourthTextColor.color())

        image.setImageResource(t.image)

    }

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { IntroductionHeaderViewHolder(it) },
                { _, item -> item is IntroductionHeaderItem }
            )

    }
}