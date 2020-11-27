package com.foryouandme.yourdata.items

import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.yourdata.YourData
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.your_data_header_item.*

data class YourDataHeaderItem(
    val configuration: Configuration,
    val id: String,
    val title: String?,
    val description: String?,
    val rating: Float?
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<YourDataHeaderItem> {
            it.id == id
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<YourDataHeaderItem> {
            it == this
        }
}

fun YourData.toYourDataHeaderItem(configuration: Configuration): YourDataHeaderItem =
    YourDataHeaderItem(
        configuration,
        id,
        title,
        body,
        starts
    )

class YourDataHeaderViewHolder(parent: ViewGroup) :
    DroidViewHolder<YourDataHeaderItem, Unit>(parent, R.layout.your_data_header_item),
    LayoutContainer {

    override fun bind(item: YourDataHeaderItem, position: Int) {
        root.setBackgroundColor(item.configuration.theme.secondaryColor.color())

        title.text = item.title.orEmpty()
        title.setTextColor(item.configuration.theme.primaryTextColor.color())

        description.text = item.description.orEmpty()
        description.setTextColor(item.configuration.theme.primaryTextColor.color())

        rating.rating = item.rating ?: 0f

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { YourDataHeaderViewHolder(it) },
                { _, item -> item is YourDataHeaderItem }
            )

    }
}