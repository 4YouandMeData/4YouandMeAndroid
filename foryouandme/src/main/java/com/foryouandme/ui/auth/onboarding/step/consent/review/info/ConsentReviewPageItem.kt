package com.foryouandme.ui.auth.onboarding.step.consent.review.info

import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.background.roundBackground
import com.foryouandme.entity.page.Page
import com.foryouandme.core.ext.html.setHtmlText
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.consent_review_page.*

data class ConsentReviewPageItem(
    val id: String,
    val configuration: Configuration,
    val title: String,
    val body: String
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        when (other) {
            is ConsentReviewPageItem -> id == other.id
            else -> false
        }

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        when (other) {
            is ConsentReviewPageItem ->
                id == other.id &&
                        title == other.title &&
                        body == other.body
            else -> false
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()
}

fun Page.toConsentReviewPageItem(configuration: Configuration) =
    ConsentReviewPageItem(
        id,
        configuration,
        title,
        body
    )

class ConsentReviewPageViewHolder(
    parent: ViewGroup
) : DroidViewHolder<ConsentReviewPageItem, Unit>(
    parent,
    { layoutInflater, viewGroup, b ->
        layoutInflater.inflate(
            R.layout.consent_review_page,
            viewGroup,
            b
        )
    }
), LayoutContainer {

    override fun bind(item: ConsentReviewPageItem, position: Int) {

        title.setHtmlText(item.title, true)
        title.setTextColor(item.configuration.theme.primaryTextColor.color())

        body.setHtmlText(item.body, true)
        body.setTextColor(item.configuration.theme.primaryTextColor.color())

        root.background =
            roundBackground(item.configuration.theme.deactiveColor.color())

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { ConsentReviewPageViewHolder(it) },
                { _, item -> item is ConsentReviewPageItem }
            )

    }

}
