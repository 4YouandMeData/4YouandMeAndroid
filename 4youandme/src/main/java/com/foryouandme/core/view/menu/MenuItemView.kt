package com.foryouandme.core.view.menu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.foryouandme.R
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.imageConfiguration
import kotlinx.android.synthetic.main.menu_item.view.*

class MenuItemView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.menu_item, this)

    }

    fun applyData(
        configuration: Configuration,
        @DrawableRes iconRes: Int,
        descriptionRes: String
    ): Unit {
        icon.setImageResource(iconRes)

        description.text = descriptionRes
        description.setTextColor(configuration.theme.primaryTextColor.color())

        arrow.setImageResource(context.imageConfiguration.arrow())
    }

}