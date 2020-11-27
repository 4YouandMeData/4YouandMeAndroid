package com.foryouandme.researchkit.step.introduction.list

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.foryouandme.R
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.step_introduction_header_item.*

data class IntroductionHeaderItem(
    val title: String,
    val titleColor: Int,
    @DrawableRes val image: Int,
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

    override fun bind(item: IntroductionHeaderItem, position: Int) {

        title.text = item.title
        title.setTextColor(item.titleColor)

        image.setImageResource(item.image)

    }

    companion object {

        fun factory(): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { IntroductionHeaderViewHolder(it) },
                { _, item -> item is IntroductionHeaderItem }
            )

    }
}