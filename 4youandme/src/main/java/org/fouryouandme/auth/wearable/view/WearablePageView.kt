package org.fouryouandme.auth.wearable.view

import android.content.Context
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import arrow.core.Option
import com.giacomoparisi.spandroid.SpanDroid
import com.giacomoparisi.spandroid.spanList
import kotlinx.android.synthetic.main.wearable_page.view.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.ext.imageConfiguration


class WearablePageView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.wearable_page, this)

    }

    fun applyData(
        configuration: Configuration,
        page: Page,
        action1: (Option<Page>) -> Unit,
        action2: ((Option<Page>) -> Unit)? = null,
        externalAction: (String) -> Unit
    ): Unit {

        title.text = page.title
        title.setTextColor(configuration.theme.primaryTextColor.color())

        description.text = page.body
        description.setTextColor(configuration.theme.primaryTextColor.color())

        shadow.background =
            HEXGradient.from(
                HEXColor.transparent(),
                configuration.theme.primaryTextColor
            ).drawable(0.3f)

        (page.link1Label).fold(
            {
                next.isVisible = true
                button_1_text.isVisible = false
                button_2_text.isVisible = false

                next.background =
                    button(
                        context.resources,
                        context.imageConfiguration.signUpNextStepSecondary()

                    )

                next.setOnClickListener { action1(page.link1) }
            },
            {
                next.isVisible = false
                button_1_text.isVisible = true

                button_1_text.setTextColor(configuration.theme.secondaryColor.color())
                button_1_text.background =
                    button(configuration.theme.primaryColorEnd.color())
                button_1_text.text = it
                button_1_text.setOnClickListener { action1(page.link1) }
            }
        )

        (page.link2Label).fold(
            {
                button_1_text.isVisible = false
            },
            {
                if (action2 != null) {
                    button_1_text.isVisible = true

                    button_1_text.setTextColor(configuration.theme.fourthTextColor.color())
                    button_1_text.text =
                        SpanDroid()
                            .append(
                                it,
                                spanList(context) { custom(UnderlineSpan()) }
                            )
                            .toSpannableString()
                    button_1_text.setOnClickListener { action2(page.link2) }

                } else button_1_text.isVisible = false
            }
        )

        page_root.setBackgroundColor(configuration.theme.secondaryColor.color())
        footer.setBackgroundColor(configuration.theme.secondaryColor.color())

    }
}