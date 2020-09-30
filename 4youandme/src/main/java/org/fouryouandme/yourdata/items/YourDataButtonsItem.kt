package org.fouryouandme.yourdata.items

import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.your_data_buttons_item.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration


data class YourDataButtonsItem(
    val configuration: Configuration,
    val imageConfiguration: ImageConfiguration,
    val id: String,
    val title: String
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<YourDataButtonsItem> {
            it.id == id
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<YourDataButtonsItem> {
            it == this
        }
}

class YourDataButtonsViewHolder(parent: ViewGroup) :
    DroidViewHolder<YourDataButtonsItem, Unit>(parent, R.layout.your_data_buttons_item),
    LayoutContainer {

    override fun bind(t: YourDataButtonsItem, position: Int) {
        root.setBackgroundColor(t.configuration.theme.fourthColor.color())

        title.text = t.title
        title.setTextColor(t.configuration.theme.primaryTextColor.color())

        buttons.setBackgroundResource(t.imageConfiguration.buttonBackground())

        filter_day.text = "DAY"
        filter_day.setTextColor(t.configuration.theme.primaryTextColor.color())
        filter_day.isSelected = true


        filter_week.text = "WEEK"
        filter_week.setTextColor(t.configuration.theme.primaryTextColor.color())
        filter_week.isSelected = false

        filter_month.text = "MONTH"
        filter_month.setTextColor(t.configuration.theme.primaryTextColor.color())
        filter_month.isSelected = false

        filter_year.text = "YEAR"
        filter_year.setTextColor(t.configuration.theme.primaryTextColor.color())
        filter_year.isSelected = false

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { YourDataButtonsViewHolder(it) },
                { _, item -> item is YourDataButtonsItem }
            )

    }
}