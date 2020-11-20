package com.foryouandme.auth.onboarding.step.integration.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.auth.onboarding.step.integration.SpecialLinkAction
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.HEXColor
import com.foryouandme.core.entity.configuration.HEXGradient
import com.foryouandme.core.entity.configuration.button.button
import com.foryouandme.core.entity.integration.IntegrationApp
import com.foryouandme.core.entity.page.Page
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.imageConfiguration
import kotlinx.android.synthetic.main.integration_page_view.view.*

// TODO: Remove use standard page
class IntegrationPageView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.integration_page_view, this)

    }

    suspend fun applyData(
        configuration: Configuration,
        page: Page,
        nextAction: (Page?) -> Unit,
        specialLinkAction: (SpecialLinkAction) -> Unit,
        externalLinkAction: (String, Page?) -> Unit
    ): Unit =
        evalOnMain {

            title.setHtmlText(page.title, true)
            title.setTextColor(configuration.theme.primaryTextColor.color())

            description.setHtmlText(page.body, true)
            description.setTextColor(configuration.theme.primaryTextColor.color())

            shadow.background =
                HEXGradient.from(
                    HEXColor.transparent(),
                    configuration.theme.primaryTextColor
                ).drawable(0.3f)


            val specialAction = page.specialLinkValue.parseSpecialLink()

            when {
                specialAction != null -> {

                    action_1.isVisible = false

                    special_action.isVisible = true
                    special_action.text =
                        page.specialLinkLabel
                            ?: when (specialAction) {
                                is SpecialLinkAction.OpenApp ->
                                    configuration.text.onboarding.integration.openAppDefault
                                is SpecialLinkAction.Download ->
                                    configuration.text.onboarding.integration.downloadButtonDefault
                            }
                    special_action.setOnClickListener { specialLinkAction(specialAction) }

                    action_1_text_secondary.isVisible = true
                    action_1_text_secondary.text =
                        page.link1Label ?: configuration.text.onboarding.integration.nextDefault

                }
                page.externalLinkUrl != null -> {
                    action_1.isVisible = false

                    special_action.isVisible = true
                    special_action.text =
                        page.externalLinkLabel
                            ?: configuration.text.onboarding.integration.loginButtonDefault
                    special_action.setOnClickListener {
                        externalLinkAction.invoke(page.externalLinkUrl, page.link1)
                    }

                    action_1_text_secondary.isVisible = true
                    action_1_text_secondary.text =
                        page.link1Label ?: configuration.text.onboarding.integration.nextDefault

                }
                else -> {

                    action_1.isVisible = true
                    special_action.isVisible = false
                    action_1_text_secondary.isVisible = false

                }
            }

            action_1.background =
                button(context.resources, context.imageConfiguration.nextStepSecondary())

            special_action.background = button(configuration.theme.primaryColorEnd.color())
            special_action.setTextColor(configuration.theme.secondaryColor.color())

            action_1_text_secondary.background = button(configuration.theme.secondaryColor.color())
            action_1_text_secondary.setTextColor(configuration.theme.primaryColorEnd.color())

            action_1_text_secondary.setOnClickListener { nextAction(page.link1) }
            action_1.setOnClickListener { nextAction(page.link1) }

            page_root.setBackgroundColor(configuration.theme.secondaryColor.color())
            footer.setBackgroundColor(configuration.theme.secondaryColor.color())

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