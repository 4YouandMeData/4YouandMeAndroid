package com.foryouandme.ui.yourdata.items

import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.core.cases.yourdata.YourDataPeriod
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.background.roundBackground
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.your_data_buttons_item.*

data class YourDataButtonsItem(
    val configuration: Configuration,
    val id: String,
    val selectedPeriod: YourDataPeriod
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

class YourDataButtonsViewHolder(
    parent: ViewGroup,
    private val onPeriodClicked: (YourDataPeriod) -> Unit
) : DroidViewHolder<YourDataButtonsItem, Unit>(
    parent,
    R.layout.your_data_buttons_item
), LayoutContainer {

    override fun bind(item: YourDataButtonsItem, position: Int) {

        val selectedBgColor = item.configuration.theme.activeColor.color()
        val bgColor = item.configuration.theme.secondaryColor.color()

        val selectedTextColor = item.configuration.theme.secondaryColor.color()
        val textColor = item.configuration.theme.fourthTextColor.color()

        root.setBackgroundColor(item.configuration.theme.fourthColor.color())

        title.text = item.configuration.text.yourData.dataPeriodTitle
        title.setTextColor(item.configuration.theme.primaryTextColor.color())

        filter_week.text = item.configuration.text.yourData.periodWeek
        filter_week.setTextColor(
            if (item.selectedPeriod == YourDataPeriod.Week) selectedTextColor else textColor
        )
        filter_week.background =
            roundBackground(
                if (item.selectedPeriod == YourDataPeriod.Week) selectedBgColor else bgColor,
                20,
                0,
                0,
                20
            )

        filter_week.setOnClickListener { onPeriodClicked(YourDataPeriod.Week) }

        filter_month.text = item.configuration.text.yourData.periodMonth
        filter_month.setTextColor(
            if (item.selectedPeriod == YourDataPeriod.Month) selectedTextColor else textColor
        )
        filter_month.setBackgroundColor(
            if (item.selectedPeriod == YourDataPeriod.Month) selectedBgColor else bgColor
        )
        filter_month.setOnClickListener { onPeriodClicked(YourDataPeriod.Month) }

        filter_year.text = item.configuration.text.yourData.periodYear
        filter_year.setTextColor(
            if (item.selectedPeriod == YourDataPeriod.Year) selectedTextColor else textColor
        )
        filter_year.background =
            roundBackground(
                if (item.selectedPeriod == YourDataPeriod.Year) selectedBgColor else bgColor,
                0,
                20,
                20,
                0
            )
        filter_year.setOnClickListener { onPeriodClicked(YourDataPeriod.Year) }

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(onPeriodClicked: (YourDataPeriod) -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { YourDataButtonsViewHolder(it, onPeriodClicked) },
                { _, item -> item is YourDataButtonsItem }
            )

    }
}