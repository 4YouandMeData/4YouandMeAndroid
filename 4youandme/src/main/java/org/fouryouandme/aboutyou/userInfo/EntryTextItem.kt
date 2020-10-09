package org.fouryouandme.aboutyou.userInfo

import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import arrow.fx.coroutines.Disposable
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.entry_text_item.*
import kotlinx.coroutines.delay
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.startCoroutineCancellableAsync

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

    override fun bind(t: EntryTextItem, position: Int) {

        name.text = t.name
        name.setTextColor(t.configuration.theme.fourthTextColor.color())

        if (entry.text.toString() != t.value) {
            entry.setText(t.value)
            entry.setSelection(entry.text?.length ?: 0)
        }

        entry.setTextColor(t.configuration.theme.primaryTextColor.color())
        entry.isEnabled = t.isEditable
        entry.clearTextChangedListeners()
        entry.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                disposable?.let {
                    it()
                }
                disposable =
                    startCoroutineCancellableAsync {

                        delay(1000)
                        onTextChange(t, entry.text?.toString().orEmpty())

                    }
            }

        })

        validation.imageTintList =
            ColorStateList.valueOf(t.configuration.theme.primaryTextColor.color())

        validation.setImageResource(
            if (t.isEditable) t.imageConfiguration.entryWrong()
            else t.imageConfiguration.entryValid()
        )

    }

    companion object {

        fun factory(onTextChange: (EntryTextItem, String) -> Unit): ViewHolderFactory =
            ViewHolderFactory(
                { EntryStringViewHolder(it, onTextChange) },
                { _, item -> item is EntryTextItem }
            )

    }

}