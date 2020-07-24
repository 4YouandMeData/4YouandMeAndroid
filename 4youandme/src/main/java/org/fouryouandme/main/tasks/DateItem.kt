package org.fouryouandme.main.tasks

import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.date_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

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
) : DroidViewHolder<DateItem, Unit>(viewGroup, R.layout.date_item), LayoutContainer {

    override fun bind(t: DateItem, position: Int) {

        date.text = t.date.format(DateTimeFormatter.ofPattern("MMMM dd yyyy", Locale.US))
        date.setTextColor(t.configuration.theme.primaryTextColor.color())

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { DateViewHolder(it) },
                { _, item -> item is DateItem }
            )

    }
}