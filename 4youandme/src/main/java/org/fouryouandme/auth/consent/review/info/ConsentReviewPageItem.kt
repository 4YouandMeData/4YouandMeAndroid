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
import org.fouryouandme.core.ext.html.setHtmlText

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

    override fun bind(t: ConsentReviewPageItem, position: Int) {

        title.setHtmlText( t.title, true)
        title.setTextColor(t.configuration.theme.primaryTextColor.color())

        body.setHtmlText(t.body, true)
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
