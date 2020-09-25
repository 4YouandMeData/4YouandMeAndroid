package org.fouryouandme.aboutyou.appsanddevices

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.device_item.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.imageConfiguration

data class AppAndDeviceItem(
    val configuration: Configuration,
    val id: String,
    val name: String,
    @DrawableRes val image: Int,
    val isConnected: Boolean,
    val link: String
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<AppAndDeviceItem> {
            it.id == id
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
        itemView.setOnClickListener{
            onItemClicked(item)
        }
    }

    override fun bind(t: AppAndDeviceItem, position: Int) {
        root.setCardBackgroundColor(t.configuration.theme.primaryColorStart.color())

        icon.setImageResource(t.image)

        name.text = t.name
        name.setTextColor(t.configuration.theme.secondaryTextColor.color())

        connect.setTextColor(t.configuration.theme.secondaryTextColor.color())

        //arrow.setImageResource(context.imageConfiguration.deactivatedButton())
        arrow.setImageResource(context.imageConfiguration.signUpNextStep())
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