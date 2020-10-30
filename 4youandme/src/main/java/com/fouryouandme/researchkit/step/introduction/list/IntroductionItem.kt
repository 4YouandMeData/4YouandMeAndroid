package com.fouryouandme.researchkit.step.introduction.list

import android.view.View
import android.view.ViewGroup
import com.fouryouandme.R
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.step_introduction_item.*

data class IntroductionItem(
    val title: String,
    val titleColor: Int,
    val description: String,
    val descriptionColor: Int
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        compare<IntroductionItem> {
            it.title == title &&
                    it.description == description
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        compare<IntroductionItem> {
            it.title == title &&
                    it.description == description
        }

}

class IntroductionViewHolder(
    parent: ViewGroup
) : DroidViewHolder<IntroductionItem, Unit>(
    parent,
    { layoutInflater, viewGroup, b ->
        layoutInflater.inflate(
            R.layout.step_introduction_item,
            viewGroup,
            b
        )
    }
), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(t: IntroductionItem, position: Int) {

        title.text = t.title
        title.setTextColor(t.titleColor)

        description.text = t.description
        description.setTextColor(t.descriptionColor)

    }

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { IntroductionViewHolder(it) },
                { _, item -> item is IntroductionItem }
            )

    }

}