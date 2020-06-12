package org.fouryouandme.core.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import arrow.core.Option
import arrow.core.getOrElse
import kotlinx.android.synthetic.main.page.view.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.ext.imageConfiguration


class PageView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.page, this)

    }


    fun applyData(
        configuration: Configuration,
        buttonText: String? = null,
        centerMessage: Boolean = false,
        page: Page,
        action: (Option<Page>) -> Unit,
        externalAction: (String) -> Unit
    ): Unit {

        val decodedString: ByteArray = Base64.decode(page.image, Base64.DEFAULT)
        val decodedByte: Bitmap =
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        icon.setImageBitmap(decodedByte)

        title.text = page.title
        title.setTextColor(configuration.theme.primaryTextColor.color())

        description.text = page.body
        description.setTextColor(configuration.theme.primaryTextColor.color())

        description.gravity = if (centerMessage) Gravity.CENTER else Gravity.START

        external.text = page.externalLinkLabel.getOrElse { "" }
        external.setTextColor(configuration.theme.primaryColorEnd.color())
        external.isVisible = page.externalLinkUrl.isDefined()
        external.setOnClickListener { page.externalLinkUrl.map { externalAction(it) } }

        shadow.background =
            HEXGradient.from(
                HEXColor.transparent(),
                configuration.theme.primaryTextColor
            ).drawable(0.3f)

        if (buttonText != null) {

            next.isVisible = false
            next_text.isVisible = true

            next_text.setTextColor(configuration.theme.secondaryColor.color())
            next_text.background =
                button(configuration.theme.primaryColorEnd.color())
            next_text.text = buttonText
            next_text.setOnClickListener { action(page.link1) }

        } else {

            next.isVisible = true
            next_text.isVisible = false

            next.background =
                button(context.resources, context.imageConfiguration.signUpNextStepSecondary())

            next.setOnClickListener { action(page.link1) }

        }

        page_root.setBackgroundColor(configuration.theme.secondaryColor.color())
        footer.setBackgroundColor(configuration.theme.secondaryColor.color())

    }
}