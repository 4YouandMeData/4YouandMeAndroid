package com.foryouandme.core.view.page

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.ext.*
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.view.page.EPageType.*
import com.foryouandme.databinding.PageBinding
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.entity.page.Page
import com.foryouandme.entity.page.PageRef
import com.giacomoparisi.spandroid.Span
import com.giacomoparisi.spandroid.SpanDroid

class PageView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private val binding =
        PageBinding.inflate(LayoutInflater.from(context), this, true)

    @Deprecated("Use the non suspend version")
    suspend fun applyDataSuspend(
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

    fun applyData(
        configuration: Configuration,
        page: Page,
        pageType: EPageType,
        action1: (PageRef?) -> Unit,
        action2: ((PageRef?) -> Unit)? = null,
        extraStringAction: ((String) -> Unit)? = null,
        extraPageAction: ((PageRef) -> Unit)? = null,
        specialStringAction: ((String) -> Unit)? = null,
        specialStringPageAction: ((String, PageRef?) -> Unit)? = null
    ) {

        setUpButtons(page, pageType, configuration, action1, action2)

        setUpImage(page, pageType)

        setUpTitleDescription(page, pageType, configuration)

        setUpExtraAction(page, extraPageAction, extraStringAction, configuration)

        setUpShadow(configuration)

        setUpButtonsClick(page, action2, specialStringAction, specialStringPageAction)

        setUpBackgrounds(configuration)

    }

    private fun setUpImage(page: Page, EPageType: EPageType) {

        val params = binding.icon.layoutParams

        params.height = if (EPageType == INFO) 60.dpToPx() else 100.dpToPx()
        params.width = if (EPageType == INFO) 60.dpToPx() else 100.dpToPx()

        binding.icon.layoutParams = params

        val decodedString = page.image?.let { Base64.decode(it, Base64.DEFAULT) }
        val decodedByte =
            decodedString?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

        decodedByte?.let { binding.icon.setImageBitmap(it) }

    }

    private fun setUpTitleDescription(
        page: Page,
        pageType: EPageType,
        configuration: Configuration
    ) {

        binding.title.setHtmlText(page.title, true)
        binding.title.setTextColor(configuration.theme.primaryTextColor.color())

        binding.description.setHtmlText(page.body, true)
        binding.description.setTextColor(configuration.theme.primaryTextColor.color())

        binding.description.gravity =
            when (pageType) {
                INFO -> Gravity.START
                FAILURE -> Gravity.CENTER
                SUCCESS -> Gravity.CENTER
            }

    }

    private fun setUpExtraAction(
        page: Page,
        extraPageAction: ((PageRef) -> Unit)?,
        extraStringAction: ((String) -> Unit)?,
        configuration: Configuration
    ) {

        when {

            extraPageAction != null && page.linkModalValue != null -> {
                binding.external.text = page.linkModalLabel.orEmpty()
                binding.external.setTextColor(configuration.theme.primaryColorEnd.color())
                binding.external.isVisible = true
                binding.external.setOnClickListener {

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
                binding.external.text = page.externalLinkLabel.orEmpty()
                binding.external.setTextColor(configuration.theme.primaryColorEnd.color())
                binding.external.isVisible = true
                binding.external.setOnClickListener {

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

            else -> binding.external.isVisible = false

        }

    }

    private fun setUpShadow(configuration: Configuration) {

        binding.shadow.background =
            HEXGradient.from(HEXColor.transparent(), configuration.theme.primaryTextColor)
                .drawable(0.3f)

    }

    private fun setUpButtons(
        page: Page,
        pageType: EPageType,
        configuration: Configuration,
        action1: (PageRef?) -> Unit,
        action2: ((PageRef?) -> Unit)?,
    ) {

        binding.action1TextSecondary.background = button(configuration.theme.secondaryColor.color())
        binding.action1Text.background = button(configuration.theme.primaryColorEnd.color())
        binding.specialAction.background = button(configuration.theme.primaryColorEnd.color())
        binding.action1.background =
            button(
                context.resources,
                when (pageType) {
                    INFO -> context.imageConfiguration.nextStepSecondary()
                    FAILURE -> context.imageConfiguration.previousStepSecondary()
                    SUCCESS -> context.imageConfiguration.nextStepSecondary()
                }
            )

        binding.action1Text.setTextColor(configuration.theme.secondaryColor.color())
        binding.action1TextSecondary.setTextColor(configuration.theme.primaryColorEnd.color())
        binding.action2Text.setTextColor(configuration.theme.fourthTextColor.color())
        binding.specialAction.setTextColor(configuration.theme.secondaryColor.color())

        binding.action1Text.text = page.link1Label
        binding.action1TextSecondary.text =
            page.link1Label ?: configuration.text.onboarding.integration.nextDefault
        binding.action2Text.text =
            SpanDroid.span()
                .append(
                    page.link2Label.orEmpty(),
                    Span.Underline
                )
                .toSpannableString()


        binding.action1.setOnClickListener { action1(page.link1) }
        binding.action1Text.setOnClickListener { action1(page.link1) }
        binding.action1TextSecondary.setOnClickListener { action1(page.link1) }
        binding.action2Text.setOnClickListener { action2?.invoke(page.link2) }


    }

    private fun setUpButtonsClick(
        page: Page,
        action2: ((PageRef?) -> Unit)?,
        specialStringAction: ((String) -> Unit)?,
        specialStringPageAction: ((String, PageRef?) -> Unit)?
    ) {

        when {

            page.specialLinkValue != null && specialStringAction != null -> {

                binding.action1.isVisible = false
                binding.action1Text.isVisible = false
                binding.action1TextSecondary.isVisible = true
                binding.specialAction.isVisible = true

                binding.specialAction.setOnClickListener { specialStringAction(page.specialLinkValue) }
                binding.specialAction.text = page.specialLinkLabel.orEmpty()

            }

            page.externalLinkUrl != null && specialStringPageAction != null -> {

                binding.action1.isVisible = false
                binding.action1Text.isVisible = false
                binding.action1TextSecondary.isVisible = true
                binding.specialAction.isVisible = true

                binding.specialAction.setOnClickListener {
                    specialStringPageAction(page.externalLinkUrl, page.link1)
                }
                binding.specialAction.text = page.specialLinkLabel.orEmpty()

            }

            page.link1Label == null -> {

                binding.action1.isVisible = true
                binding.action1Text.isVisible = false
                binding.action1TextSecondary.isVisible = false
                binding.specialAction.isVisible = false

            }

            else -> {

                binding.action1.isVisible = false
                binding.action1Text.isVisible = true
                binding.action1TextSecondary.isVisible = false
                binding.specialAction.isVisible = false

            }

        }

        if (page.link2Label == null)
            binding.action2Text.isVisible = false
        else
            binding.action2Text.isVisible = action2 != null

    }


    private fun setUpBackgrounds(configuration: Configuration) {

        binding.pageRoot.setBackgroundColor(configuration.theme.secondaryColor.color())
        binding.footer.setBackgroundColor(configuration.theme.secondaryColor.color())

    }

}