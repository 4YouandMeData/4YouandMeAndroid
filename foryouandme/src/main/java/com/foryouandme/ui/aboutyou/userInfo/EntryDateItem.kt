package com.foryouandme.ui.aboutyou.userInfo

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.entry_date_item.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

data class EntryDateItem(
    val id: String,
    val configuration: Configuration,
    val imageConfiguration: ImageConfiguration,
    val name: String,
    val value: LocalDate?,
    val isEditable: Boolean
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<EntryDateItem> { it.id == id }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<EntryDateItem> { it == this }

}

class EntryDateViewHolder(
    parent: ViewGroup,
    private val onDateSelected: (EntryDateItem, LocalDate) -> Unit
) : DroidViewHolder<EntryDateItem, Unit>(parent, R.layout.entry_date_item), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(item: EntryDateItem, position: Int) {

        name.text = item.name
        name.setTextColor(item.configuration.theme.fourthTextColor.color())

        entry.setText(
            item.value?.format(
                DateTimeFormatter.ofPattern(
                    "dd MMMM yyyy",
                    Locale.getDefault()
                )
            ).orEmpty()
        )
        entry.setTextColor(item.configuration.theme.primaryTextColor.color())
        entry.isEnabled = false

        val placeholderDate = item.value ?: LocalDate.now()

        date_button.setOnClickListener {

            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->

                    val date = LocalDate.of(
                        year,
                        month + 1,  // +1 because Android starts to count months from 0
                        dayOfMonth
                    )

                    onDateSelected(item, date)

                },
                placeholderDate.year,
                placeholderDate.monthValue,
                placeholderDate.dayOfMonth
            ).show()
        }

        date_button.isEnabled = item.isEditable

        validation.imageTintList =
            ColorStateList.valueOf(item.configuration.theme.primaryTextColor.color())

        validation.setImageResource(
            if (item.isEditable) item.imageConfiguration.entryWrong()
            else item.imageConfiguration.entryValid()
        )

    }

    companion object {

        fun factory(onDateSelected: (EntryDateItem, LocalDate) -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { EntryDateViewHolder(it, onDateSelected) },
                { _, item -> item is EntryDateItem }
            )

    }

}