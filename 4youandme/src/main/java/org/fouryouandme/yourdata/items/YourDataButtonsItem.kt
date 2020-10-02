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
import org.fouryouandme.core.cases.yourdata.YourDataPeriod
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.background.roundBackground

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

    override fun bind(t: YourDataButtonsItem, position: Int) {

        val selectedBgColor = t.configuration.theme.activeColor.color()
        val bgColor = t.configuration.theme.secondaryColor.color()

        val selectedTextColor = t.configuration.theme.secondaryColor.color()
        val textColor = t.configuration.theme.fourthTextColor.color()

        root.setBackgroundColor(t.configuration.theme.fourthColor.color())

        title.text = t.configuration.text.yourData.dataPeriodTitle
        title.setTextColor(t.configuration.theme.primaryTextColor.color())

        filter_day.text = "DAY"
        filter_day.setTextColor(
            if (t.selectedPeriod == YourDataPeriod.Day) selectedTextColor else textColor
        )
        filter_day.background =
            roundBackground(
                if (t.selectedPeriod == YourDataPeriod.Day) selectedBgColor else bgColor,
                20,
                0,
                0,
                20
            )
        filter_day.setOnClickListener { onPeriodClicked(YourDataPeriod.Day) }

        filter_week.text = "WEEK"
        filter_week.setTextColor(
            if (t.selectedPeriod == YourDataPeriod.Week) selectedTextColor else textColor
        )
        filter_week.setBackgroundColor(
            if (t.selectedPeriod == YourDataPeriod.Week) selectedBgColor else bgColor
        )
        filter_week.setOnClickListener { onPeriodClicked(YourDataPeriod.Week) }

        filter_month.text = "MONTH"
        filter_month.setTextColor(
            if (t.selectedPeriod == YourDataPeriod.Month) selectedTextColor else textColor
        )
        filter_month.setBackgroundColor(
            if (t.selectedPeriod == YourDataPeriod.Month) selectedBgColor else bgColor
        )
        filter_month.setOnClickListener { onPeriodClicked(YourDataPeriod.Month) }

        filter_year.text = "YEAR"
        filter_year.setTextColor(
            if (t.selectedPeriod == YourDataPeriod.Year) selectedTextColor else textColor
        )
        filter_year.background =
            roundBackground(
                if (t.selectedPeriod == YourDataPeriod.Year) selectedBgColor else bgColor,
                0,
                20,
                20,
                0
            )
        filter_year.setOnClickListener { onPeriodClicked(YourDataPeriod.Year) }

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(onPeriodClicked: (YourDataPeriod) -> Unit): ViewHolderFactory =
            ViewHolderFactory(
                { YourDataButtonsViewHolder(it, onPeriodClicked) },
                { _, item -> item is YourDataButtonsItem }
            )

    }
}