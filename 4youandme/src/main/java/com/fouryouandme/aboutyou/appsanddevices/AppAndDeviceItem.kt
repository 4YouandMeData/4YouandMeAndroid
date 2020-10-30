package com.fouryouandme.aboutyou.appsanddevices

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.fouryouandme.R
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.ext.imageConfiguration
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
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

    override fun bind(t: AppAndDeviceItem, position: Int) {
        root.setCardBackgroundColor(t.configuration.theme.primaryColorStart.color())

        icon.setImageResource(t.image)

        name.text = t.name
        name.setTextColor(t.configuration.theme.secondaryTextColor.color())

        connect.text = t.configuration.text.profile.connect

        if (t.isConnected.not()) {
            arrow.setImageResource(context.imageConfiguration.deactivatedButton())
            connect.setTextColor(t.configuration.theme.primaryColorEnd.color())
        } else {
            arrow.setImageResource(context.imageConfiguration.nextStep())
            connect.setTextColor(t.configuration.theme.secondaryTextColor.color())
        }
    }

    override val containerView: View? = itemView

    companion object {
        fun factory(onItemClicked: (AppAndDeviceItem) -> Unit): ViewHolderFactory =
            ViewHolderFactory({
                AppAndDeviceViewHolder(it, onItemClicked)
            },
                { _, item ->
                    item is AppAndDeviceItem
                })
    }
}