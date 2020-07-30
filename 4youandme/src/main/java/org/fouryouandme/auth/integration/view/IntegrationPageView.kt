package org.fouryouandme.auth.integration.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import arrow.core.*
import arrow.core.extensions.fx
import kotlinx.android.synthetic.main.integration_page_view.view.*
import org.fouryouandme.R
import org.fouryouandme.auth.integration.App
import org.fouryouandme.auth.integration.SpecialLinkAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.ext.imageConfiguration


class IntegrationPageView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    init {

        View.inflate(context, R.layout.integration_page_view, this)

    }

    fun applyData(
        configuration: Configuration,
        page: Page,
        nextAction: (Option<Page>) -> Unit,
        specialLinkAction: (SpecialLinkAction) -> Unit,
        externalLinkAction: (String, Option<Page>) -> Unit
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


        val specialAction = page.specialLinkValue.parseSpecialLink()

        when {
            specialAction is Some -> {

                next.isVisible = false

                button_1_text.isVisible = true
                button_1_text.text =
                    page.specialLinkLabel
                        .getOrElse {
                            when (specialAction.t) {
                                is SpecialLinkAction.OpenApp ->
                                    configuration.text.onboarding.integration.openAppDefault
                                is SpecialLinkAction.Download ->
                                    configuration.text.onboarding.integration.downloadButtonDefault

                            }
                        }
                button_1_text.setOnClickListener { specialLinkAction(specialAction.t) }

                button_2_text.isVisible = true
                button_2_text.text =
                    page.link1Label
                        .getOrElse { configuration.text.onboarding.integration.nextDefault }

            }
            page.externalLinkUrl is Some -> {

                next.isVisible = false

                button_1_text.isVisible = true
                button_1_text.text =
                    page.externalLinkLabel
                        .getOrElse { configuration.text.onboarding.integration.loginButtonDefault }
                button_1_text.setOnClickListener {
                    externalLinkAction.invoke(page.externalLinkUrl.t, page.link1)
                }

                button_2_text.isVisible = true
                button_2_text.text =
                    page.link1Label
                        .getOrElse { configuration.text.onboarding.integration.nextDefault }

            }
            else -> {

                next.isVisible = true
                button_1_text.isVisible = false
                button_2_text.isVisible = false

            }
        }

        next.background =
            button(context.resources, context.imageConfiguration.signUpNextStepSecondary())

        button_1_text.background = button(configuration.theme.primaryColorEnd.color())
        button_1_text.setTextColor(configuration.theme.secondaryColor.color())

        button_2_text.background = button(configuration.theme.secondaryColor.color())
        button_2_text.setTextColor(configuration.theme.primaryColorEnd.color())

        button_2_text.setOnClickListener { nextAction(page.link1) }
        next.setOnClickListener { nextAction(page.link1) }

        page_root.setBackgroundColor(configuration.theme.secondaryColor.color())
        footer.setBackgroundColor(configuration.theme.secondaryColor.color())

    }

    private fun Option<String>.parseSpecialLink(): Option<SpecialLinkAction> =
        Option.fx {
            !when {
                bind().contains("open", true) ->
                    SpecialLinkAction.OpenApp(!bind().parseApp()).some()
                bind().contains("download", true) ->
                    SpecialLinkAction.Download(!bind().parseApp()).some()
                else -> None
            }
        }

    private fun String.parseApp(): Option<App> =
        when {
            contains("oura", true) -> App.Oura.some()
            contains("fitbit", true) -> App.Fitbit.some()
            else -> None
        }
}