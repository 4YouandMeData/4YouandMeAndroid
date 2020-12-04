package com.foryouandme.ui.aboutyou.appsanddevices

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.foryouandme.R
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.imageConfiguration
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.compare
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.device_item.*

data class AppAndDeviceItem(
    val configuration: Configuration,
    val name: String,
    @DrawableRes val image: Int,
    val isConnected: Boolean,
    val link: String
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<AppAndDeviceItem> {
            it.name == name
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<AppAndDeviceItem> {
            it == this
        }
}

class AppAndDeviceViewHolder(parent: ViewGroup, onItemClicked: (AppAndDeviceItem) -> Unit) :
    DroidViewHolder<AppAndDeviceItem, Unit>(parent, R.layout.device_item), LayoutContainer {

    init {
        itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun bind(item: AppAndDeviceItem, position: Int) {
        root.setCardBackgroundColor(item.configuration.theme.primaryColorStart.color())

        icon.setImageResource(item.image)

        name.text = item.name
        name.setTextColor(item.configuration.theme.secondaryTextColor.color())

        connect.text = item.configuration.text.profile.connect

        if (item.isConnected.not()) {
            arrow.setImageResource(context.imageConfiguration.deactivatedButton())
            connect.setTextColor(item.configuration.theme.primaryColorEnd.color())
        } else {
            arrow.setImageResource(context.imageConfiguration.nextStep())
            connect.setTextColor(item.configuration.theme.secondaryTextColor.color())
        }
    }

    override val containerView: View? = itemView

    companion object {
        fun factory(onItemClicked: (AppAndDeviceItem) -> Unit): DroidViewHolderFactory =
            DroidViewHolderFactory({
                AppAndDeviceViewHolder(it, onItemClicked)
            },
                { _, item ->
                    item is AppAndDeviceItem
                })
    }
}