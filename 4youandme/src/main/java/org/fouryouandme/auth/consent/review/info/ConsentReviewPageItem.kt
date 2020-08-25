package org.fouryouandme.auth.consent.review.info

import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.consent_review_page.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.background.roundBackground
import org.fouryouandme.core.entity.page.Page

data class ConsentReviewPageItem(
    val id: String,
    val configuration: Configuration,
    val title: String,
    val body: String
) : DroidItem {

    override fun areTheSame(other: DroidItem): Boolean =
        when (other) {
            is ConsentReviewPageItem -> id == other.id
            else -> false
        }

    override fun haveTheSameContent(other: DroidItem): Boolean =
        when (other) {
            is ConsentReviewPageItem ->
                id == other.id &&
                        title == other.title &&
                        body == other.body
            else -> false
        }

    override fun getPayload(other: DroidItem): List<*> = emptyList<Unit>()
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

    override fun bind(t: ConsentReviewPageItem, position: Int) {

        title.text = t.title
        title.setTextColor(t.configuration.theme.primaryTextColor.color())

        body.text = t.body
        body.setTextColor(t.configuration.theme.primaryTextColor.color())

        root.background =
            roundBackground(t.configuration.theme.deactiveColor.color())

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { ConsentReviewPageViewHolder(it) },
                { _, item -> item is ConsentReviewPageItem }
            )

    }

}
