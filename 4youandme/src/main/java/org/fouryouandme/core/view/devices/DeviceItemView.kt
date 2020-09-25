package org.fouryouandme.core.view.devices

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import kotlinx.android.synthetic.main.device_item.view.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.imageConfiguration

class DeviceItemView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.device_item, this)

    }

    fun applyData(
        configuration: Configuration,
        @DrawableRes iconRes: Int,
        nameRes: String,
        linkRes: String
    ): Unit {
        root.setCardBackgroundColor(configuration.theme.primaryColorStart.color())

        icon.setImageResource(iconRes)

        name.text = nameRes
        name.setTextColor(configuration.theme.secondaryTextColor.color())

        connect.setTextColor(configuration.theme.secondaryTextColor.color())

        //arrow.setImageResource(context.imageConfiguration.deactivatedButton())
        arrow.setImageResource(context.imageConfiguration.signUpNextStep())

        setOnClickListener {

        }
    }

}