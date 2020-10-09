package org.fouryouandme.aboutyou.userInfo

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.entry_string_item.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration

data class EntryStringItem(
    val id: String,
    val configuration: Configuration,
    val imageConfiguration: ImageConfiguration,
    val name: String,
    val value: String,
    val isEditable: Boolean
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<EntryStringItem> { it.id == id }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<EntryStringItem> { it == this }

}

class EntryStringViewHolder(
    parent: ViewGroup
) : DroidViewHolder<EntryStringItem, Unit>(parent, R.layout.entry_string_item), LayoutContainer {

    override val containerView: View? = itemView

    override fun bind(t: EntryStringItem, position: Int) {

        name.text = t.name
        name.setTextColor(t.configuration.theme.fourthTextColor.color())

        entry.setText(t.value)
        entry.setTextColor(t.configuration.theme.primaryTextColor.color())
        entry.isEnabled = t.isEditable

        validation.imageTintList =
            ColorStateList.valueOf(t.configuration.theme.primaryTextColor.color())

        validation.setImageResource(
            if (t.value.isNotEmpty())
                t.imageConfiguration.entryValid()
            else
                t.imageConfiguration.entryWrong()
        )

        validation.isVisible = t.isEditable

    }

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { EntryStringViewHolder(it) },
                { _, item -> item is EntryStringItem }
            )

    }

}