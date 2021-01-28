package com.foryouandme.core.items

import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.button.button
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.util.EmptyViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.paged_request_error.*

object PagedRequestLoadingItem : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<PagedRequestLoadingItem> { true }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<PagedRequestLoadingItem> { true }
}

class PagedRequestLoadingViewHolder(
    parent: ViewGroup
) : EmptyViewHolder<PagedRequestLoadingItem>(parent, R.layout.paged_request_loading) {

    companion object {

        fun factory(): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { PagedRequestLoadingViewHolder(it) },
                { _, item -> item is PagedRequestLoadingItem }
            )

    }

}

data class PagedRequestErrorItem(
    val configuration: Configuration,
    val message: String
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<PagedRequestLoadingItem> { true }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<PagedRequestLoadingItem> { true }
}

class PagedRequestErrorViewHolder(
    parent: ViewGroup,
    private val onRetryClicked: () -> Unit
) : DroidViewHolder<PagedRequestErrorItem, Unit>(
    parent,
    { layoutInflater, viewGroup, b ->
        layoutInflater.inflate(
            R.layout.paged_request_error,
            viewGroup,
            b
        )
    }
), LayoutContainer {

    override fun bind(item: PagedRequestErrorItem, position: Int) {

        retry.setOnClickListener { this.onRetryClicked() }
        retry.background = button(item.configuration.theme.primaryColorEnd.color())
        retry.text = item.configuration.text.error.buttonRetry
        retry.setTextColor(item.configuration.theme.secondaryColor.color())

        message.setTextColor(item.configuration.theme.primaryTextColor.color())
        message.text = item.message

    }


    override val containerView: View? = this.itemView

    companion object {

        fun factory(
            onRetryClicked: () -> Unit
        ): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { PagedRequestErrorViewHolder(it, onRetryClicked) },
                { _, item -> item is PagedRequestErrorItem }
            )

    }
}
