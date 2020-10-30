package com.fouryouandme.yourdata.items

import android.view.View
import android.view.ViewGroup
import com.fouryouandme.R
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.configuration.button.button
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.your_data_error.*

data class YourDataGraphErrorItem(
    val configuration: Configuration,
    val id: String,
    val error: FourYouAndMeError
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

    override fun bind(t: YourDataGraphErrorItem, position: Int) {

        title.setTextColor(t.configuration.theme.primaryTextColor.color())
        title.text = t.error.title(context)

        description.setTextColor(t.configuration.theme.primaryTextColor.color())
        description.text = t.error.message(context)

        button.background = button(t.configuration.theme.primaryColorEnd.color())
        button.setTextColor(t.configuration.theme.secondaryColor.color())
        button.text = t.configuration.text.error.buttonRetry
        button.setOnClickListener { onButtonClicked() }

    }

    companion object {

        fun factory(onButtonClicked: () -> Unit): ViewHolderFactory =
            ViewHolderFactory(
                { YourDataGraphErrorViewHolder(it, onButtonClicked) },
                { _, item -> item is YourDataGraphErrorItem }
            )

    }

}