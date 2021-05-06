package com.foryouandme.ui.aboutyou.permissions

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.foryouandme.R
import com.foryouandme.core.cases.permission.Permission
import com.foryouandme.entity.configuration.Configuration
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.permission_item.*

data class PermissionsItemOld(
    val configuration: Configuration,
    val id: String,
    val permission: Permission,
    val description: String,
    @DrawableRes val image: Int,
    val isAllowed: Boolean
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<PermissionsItemOld> {
            it.id == id
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<PermissionsItemOld> {
            it == this
        }
}

class PermissionsViewHolder(parent: ViewGroup, onItemClicked: (PermissionsItemOld) -> Unit) :
    DroidViewHolder<PermissionsItemOld, Unit>(parent, R.layout.permission_item), LayoutContainer {

    init {
        itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun bind(item: PermissionsItemOld, position: Int) {
        root.setCardBackgroundColor(item.configuration.theme.primaryColorStart.color())

        icon.setImageResource(item.image)

        description.text = item.description
        description.setTextColor(item.configuration.theme.secondaryTextColor.color())

        allow.paintFlags = allow.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        if (item.isAllowed.not()) {
            allow.text = item.configuration.text.profile.permissionAllow
            allow.setTextColor(item.configuration.theme.secondaryTextColor.color())
        } else {
            allow.text = item.configuration.text.profile.permissionAllowed
            allow.setTextColor(item.configuration.theme.primaryColorEnd.color())
        }
    }

    override val containerView: View? = itemView

    companion object {
        fun factory(onItemClicked: (PermissionsItemOld) -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory({
                PermissionsViewHolder(it, onItemClicked)
            },
                { _, item ->
                    item is PermissionsItemOld
                })
    }
}