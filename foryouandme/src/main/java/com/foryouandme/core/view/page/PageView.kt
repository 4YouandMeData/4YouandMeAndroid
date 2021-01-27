package com.foryouandme.core.view.page

import android.content.Context
import android.graphics.BitmapFactory
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.entity.page.Page
import com.foryouandme.core.ext.*
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.view.page.EPageType.*
import com.foryouandme.entity.page.PageRef
import com.giacomoparisi.spandroid.SpanDroid
import com.giacomoparisi.spandroid.spanList
import kotlinx.android.synthetic.main.page.view.*

class PageView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.page, this)

    }

    suspend fun applyData(
        configuration: Configuration,
        page: Page,
        pageType: EPageType,
        action1: (PageRef?) -> Unit,
        action2: ((PageRef?) -> Unit)? = null,
        extraStringAction: ((String) -> Unit)? = null,
        extraPageAction: ((PageRef) -> Unit)? = null,
        specialStringAction: ((String) -> Unit)? = null,
        specialStringPageAction: ((String, PageRef?) -> Unit)? = null
    ): Unit =

        evalOnMain {

            setUpButtons(page, pageType, configuration, action1, action2)

            setUpImage(page, pageType)

            setUpTitleDescription(page, pageType, configuration)

            setUpExtraAction(page, extraPageAction, extraStringAction, configuration)

            setUpShadow(configuration)

            setUpButtonsClick(page, action2, specialStringAction, specialStringPageAction)

            setUpBackgrounds(configuration)

        }

    private suspend fun setUpImage(page: Page, EPageType: EPageType): Unit =
        evalOnMain {

            val params = icon.layoutParams

            params.height = if (EPageType == INFO) 60.dpToPx() else 100.dpToPx()
            params.width = if (EPageType == INFO) 60.dpToPx() else 100.dpToPx()

            icon.layoutParams = params

            val decodedString = page.image?.let { Base64.decode(it, Base64.DEFAULT) }
            val decodedByte =
                decodedString?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

            decodedByte?.let { icon.setImageBitmap(it) }

        }

    private suspend fun setUpTitleDescription(
        page: Page,
        pageType: EPageType,
        configuration: Configuration
    ): Unit =
        evalOnMain {

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

        }

    private suspend fun setUpExtraAction(
        page: Page,
        extraPageAction: ((PageRef) -> Unit)?,
        extraStringAction: ((String) -> Unit)?,
        configuration: Configuration
    ): Unit =
        evalOnMain {

            when {

                extraPageAction != null && page.linkModalValue != null -> {
                    external.text = page.linkModalLabel.orEmpty()
                    external.setTextColor(configuration.theme.primaryColorEnd.color())
                    external.isVisible = true
                    external.setOnClickListener {

                        startCoroutineAsync {
                            context.injector
                                .analyticsModule()
                                .logEvent(
                                    AnalyticsEvent.ScreenViewed.LearnMore,
                                    EAnalyticsProvider.ALL
                                )
                        }

                        extraPageAction(page.linkModalValue)

                    }
                }

                extraStringAction != null && page.externalLinkUrl != null -> {
                    external.text = page.externalLinkLabel.orEmpty()
                    external.setTextColor(configuration.theme.primaryColorEnd.color())
                    external.isVisible = true
                    external.setOnClickListener {

                        startCoroutineAsync {
                            context.injector
                                .analyticsModule()
                                .logEvent(
                                    AnalyticsEvent.ScreenViewed.LearnMore,
                                    EAnalyticsProvider.ALL
                                )
                        }

                        extraStringAction(page.externalLinkUrl)
                    }
                }

                else -> external.isVisible = false

            }

        }

    private suspend fun setUpShadow(configuration: Configuration): Unit =
        evalOnMain {

            shadow.background =
                HEXGradient.from(HEXColor.transparent(), configuration.theme.primaryTextColor)
                    .drawable(0.3f)

        }

    private suspend fun setUpButtons(
        page: Page,
        pageType: EPageType,
        configuration: Configuration,
        action1: (PageRef?) -> Unit,
        action2: ((PageRef?) -> Unit)?,
    ): Unit =
        evalOnMain {

            action_1_text_secondary.background = button(configuration.theme.secondaryColor.color())
            action_1_text.background = button(configuration.theme.primaryColorEnd.color())
            special_action.background = button(configuration.theme.primaryColorEnd.color())
            action_1.background =
                button(
                    context.resources,
                    when (pageType) {
                        INFO -> context.imageConfiguration.nextStepSecondary()
                        FAILURE -> context.imageConfiguration.previousStepSecondary()
                        SUCCESS -> context.imageConfiguration.nextStepSecondary()
                    }
                )

            action_1_text.setTextColor(configuration.theme.secondaryColor.color())
            action_1_text_secondary.setTextColor(configuration.theme.primaryColorEnd.color())
            action_2_text.setTextColor(configuration.theme.fourthTextColor.color())
            special_action.setTextColor(configuration.theme.secondaryColor.color())

            action_1_text.text = page.link1Label
            action_1_text_secondary.text =
                page.link1Label ?: configuration.text.onboarding.integration.nextDefault
            action_2_text.text =
                SpanDroid()
                    .append(
                        page.link2Label.orEmpty(),
                        spanList(context) { custom(UnderlineSpan()) }
                    )
                    .toSpannableString()


            action_1.setOnClickListener { action1(page.link1) }
            action_1_text.setOnClickListener { action1(page.link1) }
            action_1_text_secondary.setOnClickListener { action1(page.link1) }
            action_2_text.setOnClickListener { action2?.invoke(page.link2) }


        }

    private suspend fun setUpButtonsClick(
        page: Page,
        action2: ((PageRef?) -> Unit)?,
        specialStringAction: ((String) -> Unit)?,
        specialStringPageAction: ((String, PageRef?) -> Unit)?
    ): Unit =
        evalOnMain {

            when {

                page.specialLinkValue != null && specialStringAction != null -> {

                    action_1.isVisible = false
                    action_1_text.isVisible = false
                    action_1_text_secondary.isVisible = true
                    special_action.isVisible = true

                    special_action.setOnClickListener { specialStringAction(page.specialLinkValue) }
                    special_action.text = page.specialLinkLabel.orEmpty()

                }

                page.externalLinkUrl != null && specialStringPageAction != null -> {

                    action_1.isVisible = false
                    action_1_text.isVisible = false
                    action_1_text_secondary.isVisible = true
                    special_action.isVisible = true

                    special_action.setOnClickListener {
                        specialStringPageAction(page.externalLinkUrl, page.link1)
                    }
                    special_action.text = page.specialLinkLabel.orEmpty()

                }

                page.link1Label == null -> {

                    action_1.isVisible = true
                    action_1_text.isVisible = false
                    action_1_text_secondary.isVisible = false
                    special_action.isVisible = false

                }

                else -> {

                    action_1.isVisible = false
                    action_1_text.isVisible = true
                    action_1_text_secondary.isVisible = false
                    special_action.isVisible = false

                }

            }

            if (page.link2Label == null)
                action_2_text.isVisible = false
            else
                action_2_text.isVisible = action2 != null

        }


    private suspend fun setUpBackgrounds(configuration: Configuration): Unit =
        evalOnMain {

            page_root.setBackgroundColor(configuration.theme.secondaryColor.color())
            footer.setBackgroundColor(configuration.theme.secondaryColor.color())

        }

}