package com.fouryouandme.auth.consent.review.info

import android.view.View
import android.view.ViewGroup
import com.fouryouandme.R
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.consent.review.ConsentReview
import com.fouryouandme.core.ext.html.setHtmlText
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.consent_review_header.*

data class ConsentReviewHeaderItem(
    val configuration: Configuration,
    val title: String,
    val body: String,
    val subtitle: String,
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        when (other) {
            is ConsentReviewHeaderItem -> title == other.title
            else -> false
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        when (other) {
            is ConsentReviewHeaderItem ->
                title == other.title &&
                        body == other.body &&
                        subtitle == other.subtitle
            else -> false
        }
}

fun ConsentReview.toConsentReviewHeaderItem(
    configuration: Configuration,
): ConsentReviewHeaderItem =
    ConsentReviewHeaderItem(
        configuration,
        title,
        body,
        pagesSubtitle,
    )

class ConsentReviewHeaderViewHolder(
    parent: ViewGroup
) : DroidViewHolder<ConsentReviewHeaderItem, Unit>(
    parent,
    { layoutInflater, viewGroup, b ->
        layoutInflater.inflate(
            R.layout.consent_review_header, viewGroup, b
        )
    }
), LayoutContainer {

    override fun bind(t: ConsentReviewHeaderItem, position: Int) {

        title.setHtmlText(t.title, true)
        title.setTextColor(t.configuration.theme.primaryTextColor.color())

        body.setHtmlText(t.body, true)
        body.setTextColor(t.configuration.theme.fourthTextColor.color())

        subtitle.text = t.subtitle
        subtitle.setTextColor(t.configuration.theme.primaryTextColor.color())

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { ConsentReviewHeaderViewHolder(it) },
                { _, item -> item is ConsentReviewHeaderItem }
            )
    }
}