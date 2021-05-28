package com.foryouandme.ui.auth.onboarding.step.integration.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.databinding.IntegrationPageViewBinding
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.entity.integration.IntegrationApp
import com.foryouandme.entity.page.Page
import com.foryouandme.entity.page.PageRef
import com.foryouandme.ui.auth.onboarding.step.integration.SpecialLinkAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: Remove use standard page
@AndroidEntryPoint
class IntegrationPageView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    @Inject lateinit var imageConfiguration: ImageConfiguration

    val binding =
        IntegrationPageViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    fun applyData(
        configuration: Configuration,
        page: Page,
        nextAction: (PageRef?) -> Unit,
        specialLinkAction: (SpecialLinkAction) -> Unit,
        externalLinkAction: (String, PageRef?) -> Unit
    ) {

        binding.title.setHtmlText(page.title, true)
        binding.title.setTextColor(configuration.theme.primaryTextColor.color())

        binding.description.setHtmlText(page.body, true)
        binding.description.setTextColor(configuration.theme.primaryTextColor.color())

        binding.shadow.background =
            HEXGradient.from(
                HEXColor.transparent(),
                configuration.theme.primaryTextColor
            ).drawable(0.3f)


        val specialAction = page.specialLinkValue.parseSpecialLink()

        when {
            specialAction != null -> {

                binding.action1.isVisible = false

                binding.specialAction.isVisible = true
                binding.specialAction.text =
                    page.specialLinkLabel
                        ?: when (specialAction) {
                            is SpecialLinkAction.OpenApp ->
                                configuration.text.onboarding.integration.openAppDefault
                            is SpecialLinkAction.Download ->
                                configuration.text.onboarding.integration.downloadButtonDefault
                        }
                binding.specialAction.setOnClickListener { specialLinkAction(specialAction) }

                binding.action1TextSecondary.isVisible = true
                binding.action1TextSecondary.text =
                    page.link1Label ?: configuration.text.onboarding.integration.nextDefault

            }
            page.externalLinkUrl != null -> {
                binding.action1.isVisible = false

                binding.specialAction.isVisible = true
                binding.specialAction.text =
                    page.externalLinkLabel
                        ?: configuration.text.onboarding.integration.loginButtonDefault
                binding.specialAction.setOnClickListener {
                    externalLinkAction.invoke(page.externalLinkUrl, page.link1)
                }

                binding.action1TextSecondary.isVisible = true
                binding.action1TextSecondary.text =
                    page.link1Label ?: configuration.text.onboarding.integration.nextDefault

            }
            else -> {

                binding.action1.isVisible = true
                binding.specialAction.isVisible = false
                binding.action1TextSecondary.isVisible = false

            }
        }

        binding.action1.background =
            button(context.resources, imageConfiguration.nextStepSecondary())

        binding.specialAction.background = button(configuration.theme.primaryColorEnd.color())
        binding.specialAction.setTextColor(configuration.theme.secondaryColor.color())

        binding.action1TextSecondary.background = button(configuration.theme.secondaryColor.color())
        binding.action1TextSecondary.setTextColor(configuration.theme.primaryColorEnd.color())

        binding.action1TextSecondary.setOnClickListener { nextAction(page.link1) }
        binding.action1.setOnClickListener { nextAction(page.link1) }

        binding.pageRoot.setBackgroundColor(configuration.theme.secondaryColor.color())
        binding.footer.setBackgroundColor(configuration.theme.secondaryColor.color())

    }

    private fun String?.parseSpecialLink(): SpecialLinkAction? =
        when {
            this == null -> null
            contains("open", true) ->
                IntegrationApp.fromIdentifier(this)?.let { SpecialLinkAction.OpenApp(it) }
            contains("download", true) ->
                IntegrationApp.fromIdentifier(this)?.let { SpecialLinkAction.Download(it) }
            else -> null
        }
}