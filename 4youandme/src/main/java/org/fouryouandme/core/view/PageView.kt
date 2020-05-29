package org.fouryouandme.core.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Base64
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.page.view.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.ext.imageConfiguration


class PageView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.page, this)

    }


    fun applyData(configuration: Configuration, page: Page, nextAction: () -> Unit): Unit {

        val decodedString: ByteArray = Base64.decode(page.image, Base64.DEFAULT)
        val decodedByte: Bitmap =
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        icon.setImageBitmap(decodedByte)

        title.text = page.title
        title.setTextColor(configuration.theme.primaryTextColor.color())

        description.text = page.body
        description.setTextColor(configuration.theme.primaryTextColor.color())

        next.background = button(context.resources, context.imageConfiguration.signUpNextStepSecondary())

        next.setOnClickListener { nextAction() }

        page_root.setBackgroundColor(configuration.theme.secondaryColor.color())
        footer.setBackgroundColor(configuration.theme.secondaryColor.color())

    }
}