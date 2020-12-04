package com.foryouandme.ui.aboutyou.userInfo

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.foryouandme.R
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.entity.configuration.Configuration
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.entry_picker_item.*

data class EntryPickerValue(val id: String, val name: String)

data class EntryPickerItem(
    val id: String,
    val configuration: Configuration,
    val imageConfiguration: ImageConfiguration,
    val name: String,
    val value: EntryPickerValue?,
    val items: List<EntryPickerValue>,
    val isEditable: Boolean
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<EntryPickerItem> { it.id == id }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<EntryPickerItem> { it == this }

}

class EntryPickerViewHolder(
    parent: ViewGroup,
    private val onItemSelected: (EntryPickerItem, EntryPickerValue) -> Unit
) : DroidViewHolder<EntryPickerItem, Unit>(parent, R.layout.entry_picker_item), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(item: EntryPickerItem, position: Int) {

        name.text = item.name
        name.setTextColor(item.configuration.theme.fourthTextColor.color())

        entry.setText(item.value?.name.orEmpty())
        entry.setTextColor(item.configuration.theme.primaryTextColor.color())
        entry.isEnabled = false

        picker_button.setOnClickListener {

            AlertDialog.Builder(context)
                .setItems(
                    item.items.map { it.name }.toTypedArray()
                ) { dialog, which ->

                    item.items.getOrNull(which)?.let { onItemSelected(item, it) }
                    dialog.dismiss()

                }
                .show()

        }
        picker_button.isEnabled = item.isEditable

        validation.imageTintList =
            ColorStateList.valueOf(item.configuration.theme.primaryTextColor.color())

        validation.setImageResource(
            if (item.isEditable) item.imageConfiguration.entryWrong()
            else item.imageConfiguration.entryValid()
        )

    }

    companion object {

        fun factory(
            onItemSelected: (EntryPickerItem, EntryPickerValue) -> Unit
        ): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { EntryPickerViewHolder(it, onItemSelected) },
                { _, item -> item is EntryPickerItem }
            )

    }

}