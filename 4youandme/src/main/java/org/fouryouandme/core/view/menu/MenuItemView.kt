package org.fouryouandme.core.view.menu

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.menu_item.view.*
import org.fouryouandme.R
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration

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

        description.setText(descriptionRes)
        description.setTextColor(configuration.theme.primaryTextColor.color())

        arrow.setImageResource(context.imageConfiguration.arrow())
    }

}