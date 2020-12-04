package com.foryouandme.ui.main.items

import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.entity.configuration.Configuration
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.date_item.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

data class DateItem(val configuration: Configuration, val date: LocalDate) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        when (other) {
            is DateItem -> date.isEqual(other.date)
            else -> false
        }

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        when (other) {
            is DateItem -> date.isEqual(other.date)
            else -> false
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

}

class DateViewHolder(
    viewGroup: ViewGroup
) : DroidViewHolder<DateItem, Unit>(viewGroup, R.layout.date_item), LayoutContainer {

    override fun bind(item: DateItem, position: Int) {

        date.text = item.date.format(DateTimeFormatter.ofPattern("MMMM dd yyyy", Locale.US))
        date.setTextColor(item.configuration.theme.primaryTextColor.color())

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { DateViewHolder(it) },
                { _, item -> item is DateItem }
            )

    }
}