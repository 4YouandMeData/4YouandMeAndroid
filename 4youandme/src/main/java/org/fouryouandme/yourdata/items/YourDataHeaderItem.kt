package org.fouryouandme.yourdata.items

import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.your_data_header_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.yourdata.YourData

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
        starts?.toFloat()
    )

class YourDataHeaderViewHolder(parent: ViewGroup) :
    DroidViewHolder<YourDataHeaderItem, Unit>(parent, R.layout.your_data_header_item),
    LayoutContainer {

    override fun bind(t: YourDataHeaderItem, position: Int) {
        root.setBackgroundColor(t.configuration.theme.secondaryColor.color())

        title.text = t.title.orEmpty()
        title.setTextColor(t.configuration.theme.primaryTextColor.color())

        description.text = t.description.orEmpty()
        description.setTextColor(t.configuration.theme.primaryTextColor.color())

        rating.rating = t.rating ?: 0f

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { YourDataHeaderViewHolder(it) },
                { _, item -> item is YourDataHeaderItem }
            )

    }
}