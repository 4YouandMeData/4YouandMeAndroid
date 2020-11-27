package com.foryouandme.aboutyou.userInfo

import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import arrow.fx.coroutines.Disposable
import arrow.fx.coroutines.cancelBoundary
import com.foryouandme.R
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.startCoroutineCancellableAsync
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.entry_text_item.*
import kotlinx.coroutines.delay

data class EntryTextItem(
    val id: String,
    val configuration: Configuration,
    val imageConfiguration: ImageConfiguration,
    val name: String,
    val value: String,
    val isEditable: Boolean
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<EntryTextItem> { it.id == id }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<EntryTextItem> { it == this }

}

class EntryStringViewHolder(
    parent: ViewGroup,
    private val onTextChange: (EntryTextItem, String) -> Unit
) : DroidViewHolder<EntryTextItem, Unit>(parent, R.layout.entry_text_item), LayoutContainer {

    var disposable: Disposable? = null

    override val containerView: View? = itemView

    override fun bind(item: EntryTextItem, position: Int) {

        name.text = item.name
        name.setTextColor(item.configuration.theme.fourthTextColor.color())

        if (entry.text.toString() != item.value) {
            entry.setText(item.value)
            entry.setSelection(entry.text?.length ?: 0)
        }

        entry.setTextColor(item.configuration.theme.primaryTextColor.color())
        entry.isEnabled = item.isEditable
        entry.clearTextChangedListeners()
        entry.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                disposable?.let { it() }
                disposable =
                    startCoroutineCancellableAsync {

                        delay(500)
                        cancelBoundary()
                        onTextChange(item, entry.text?.toString().orEmpty())

                    }
            }

        })

        validation.imageTintList =
            ColorStateList.valueOf(item.configuration.theme.primaryTextColor.color())

        validation.setImageResource(
            if (item.isEditable) item.imageConfiguration.entryWrong()
            else item.imageConfiguration.entryValid()
        )

    }

    companion object {

        fun factory(onTextChange: (EntryTextItem, String) -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { EntryStringViewHolder(it, onTextChange) },
                { _, item -> item is EntryTextItem }
            )

    }

}