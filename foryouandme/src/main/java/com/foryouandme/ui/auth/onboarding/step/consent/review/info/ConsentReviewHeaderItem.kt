package com.foryouandme.ui.auth.onboarding.step.consent.review.info

import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.consent.review.ConsentReview
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.databinding.ConsentReviewHeaderBinding
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory

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
) {

    override fun bind(item: ConsentReviewHeaderItem, position: Int) {

        val binding = ConsentReviewHeaderBinding.bind(itemView)

        binding.title.setHtmlText(item.title, true)
        binding.title.setTextColor(item.configuration.theme.primaryTextColor.color())

        binding.body.setHtmlText(item.body, true)
        binding.body.setTextColor(item.configuration.theme.fourthTextColor.color())

        binding.subtitle.text = item.subtitle
        binding.subtitle.setTextColor(item.configuration.theme.primaryTextColor.color())

    }

    companion object {

        fun factory(): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { ConsentReviewHeaderViewHolder(it) },
                { _, item -> item is ConsentReviewHeaderItem }
            )

    }
}