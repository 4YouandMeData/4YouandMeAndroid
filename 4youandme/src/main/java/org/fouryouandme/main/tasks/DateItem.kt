package org.fouryouandme.main.tasks

import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.threeten.bp.ZonedDateTime

data class DateItem(val configuration: Configuration, val date: ZonedDateTime) : DroidItem {

    override fun areTheSame(other: DroidItem): Boolean =
        when (other) {
            is DateItem -> date.isEqual(other.date)
            else -> false
        }

    override fun haveTheSameContent(other: DroidItem): Boolean =
        when (other) {
            is DateItem -> date.isEqual(other.date)
            else -> false
        }

    override fun getPayload(other: DroidItem): List<*> = emptyList<Unit>()

}

class DateViewHolder(
    viewGroup: ViewGroup
): DroidViewHolder<DateItem, Unit>(viewGroup, R.layout.date_item) {

    override fun bind(t: DateItem, position: Int) {

    }

}