package org.fouryouandme.aboutyou.permissions

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.permission_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration

data class PermissionsItem(
    val configuration: Configuration,
    val id: String,
    val permission: String,
    val description: String,
    @DrawableRes val image: Int,
    val isAllowed: Boolean
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<PermissionsItem> {
            it.id == id
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<PermissionsItem> {
            it == this
        }
}

class PermissionsViewHolder(parent: ViewGroup, onItemClicked: (PermissionsItem) -> Unit) :
    DroidViewHolder<PermissionsItem, Unit>(parent, R.layout.permission_item), LayoutContainer {

    init {
        itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun bind(t: PermissionsItem, position: Int) {
        root.setCardBackgroundColor(t.configuration.theme.primaryColorStart.color())

        icon.setImageResource(t.image)

        description.text = t.description
        description.setTextColor(t.configuration.theme.secondaryTextColor.color())

        allow.paintFlags = allow.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        if (t.isAllowed.not()) {
            allow.text = t.configuration.text.profile.allow
            allow.setTextColor(t.configuration.theme.secondaryTextColor.color())
        } else {
            allow.text = t.configuration.text.profile.allowed
            allow.setTextColor(t.configuration.theme.primaryColorEnd.color())
        }
    }

    override val containerView: View? = itemView

    companion object {
        fun factory(onItemClicked: (PermissionsItem) -> Unit): ViewHolderFactory =
            ViewHolderFactory({
                PermissionsViewHolder(it, onItemClicked)
            },
                { _, item ->
                    item is PermissionsItem
                })
    }
}