package org.fouryouandme.core.view.page

import android.content.Context
import android.graphics.BitmapFactory
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.giacomoparisi.spandroid.SpanDroid
import com.giacomoparisi.spandroid.spanList
import kotlinx.android.synthetic.main.page.view.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.ext.dpToPx
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.html.setHtmlText
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.view.page.EPageType.*


class PageView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.page, this)

    }

    suspend fun applyData(
        configuration: Configuration,
        page: Page,
        pageType: EPageType,
        action1: (Page?) -> Unit,
        action2: ((Page?) -> Unit)? = null,
        externalAction: ((String) -> Unit)? = null,
        modalAction: ((Page) -> Unit)? = null
    ): Unit =

        evalOnMain {

            val params = icon.layoutParams

            params.height = if (pageType == INFO) 60.dpToPx() else 100.dpToPx()
            params.width = if (pageType == INFO) 60.dpToPx() else 100.dpToPx()

            icon.layoutParams = params

            val decodedString = page.image?.let { Base64.decode(it, Base64.DEFAULT) }
            val decodedByte =
                decodedString?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

            decodedByte?.let { icon.setImageBitmap(it) }

            title.setHtmlText(page.title, true)
            title.setTextColor(configuration.theme.primaryTextColor.color())

            description.setHtmlText(page.body, true)
            description.setTextColor(configuration.theme.primaryTextColor.color())

            description.gravity =
                when (pageType) {
                    INFO -> Gravity.START
                    FAILURE -> Gravity.CENTER
                    SUCCESS -> Gravity.CENTER
                }

            if (modalAction != null) {
                external.text = page.linkModalLabel.orEmpty()
                external.setTextColor(configuration.theme.primaryColorEnd.color())
                external.isVisible = page.linkModalValue != null
                external.setOnClickListener { page.linkModalValue?.let { modalAction(it) } }
            } else if (externalAction != null) {
                external.text = page.externalLinkLabel.orEmpty()
                external.setTextColor(configuration.theme.primaryColorEnd.color())
                external.isVisible = page.externalLinkUrl != null
                external.setOnClickListener { page.externalLinkUrl?.let { externalAction(it) } }
            } else external.isVisible = false

            shadow.background =
                HEXGradient.from(
                    HEXColor.transparent(),
                    configuration.theme.primaryTextColor
                ).drawable(0.3f)

            if (page.link1Label == null) {
                next.isVisible = true
                next_text.isVisible = false

                next.background =
                    button(
                        context.resources,
                        when (pageType) {
                            INFO -> context.imageConfiguration.signUpNextStepSecondary()
                            FAILURE -> context.imageConfiguration.signUpPreviousStepSecondary()
                            SUCCESS -> context.imageConfiguration.signUpNextStepSecondary()
                        }
                    )

                next.setOnClickListener { action1(page.link1) }
            } else {
                next.isVisible = false
                next_text.isVisible = true

                next_text.setTextColor(configuration.theme.secondaryColor.color())
                next_text.background =
                    button(configuration.theme.primaryColorEnd.color())
                next_text.text = page.link1Label
                next_text.setOnClickListener { action1(page.link1) }
            }

            if (page.link2Label == null)
                next_2_text.isVisible = false
            else {
                if (action2 != null) {
                    next_2_text.isVisible = true

                    next_2_text.setTextColor(configuration.theme.fourthTextColor.color())
                    next_2_text.text =
                        SpanDroid()
                            .append(
                                page.link2Label,
                                spanList(context) { custom(UnderlineSpan()) }
                            )
                            .toSpannableString()
                    next_2_text.setOnClickListener { action2(page.link2) }

                } else next_2_text.isVisible = false
            }

            page_root.setBackgroundColor(configuration.theme.secondaryColor.color())
            footer.setBackgroundColor(configuration.theme.secondaryColor.color())

        }
}