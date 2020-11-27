package com.foryouandme.yourdata.items

import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.button.button
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.your_data_error.*

data class YourDataGraphErrorItem(
    val configuration: Configuration,
    val id: String,
    val error: ForYouAndMeError
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<YourDataGraphErrorItem> { it.id == id }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<YourDataGraphErrorItem> { it == this }

}

class YourDataGraphErrorViewHolder(
    viewGroup: ViewGroup,
    private val onButtonClicked: () -> Unit
) : DroidViewHolder<YourDataGraphErrorItem, Unit>(
    viewGroup,
    R.layout.your_data_error
), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(item: YourDataGraphErrorItem, position: Int) {

        title.setTextColor(item.configuration.theme.primaryTextColor.color())
        title.text = item.error.title(context)

        description.setTextColor(item.configuration.theme.primaryTextColor.color())
        description.text = item.error.message(context)

        button.background = button(item.configuration.theme.primaryColorEnd.color())
        button.setTextColor(item.configuration.theme.secondaryColor.color())
        button.text = item.configuration.text.error.buttonRetry
        button.setOnClickListener { onButtonClicked() }

    }

    companion object {

        fun factory(onButtonClicked: () -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { YourDataGraphErrorViewHolder(it, onButtonClicked) },
                { _, item -> item is YourDataGraphErrorItem }
            )

    }

}