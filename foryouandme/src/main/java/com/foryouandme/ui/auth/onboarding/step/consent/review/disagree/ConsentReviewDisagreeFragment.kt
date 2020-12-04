package com.foryouandme.ui.auth.onboarding.step.consent.review.disagree

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.consent.review.ConsentReviewSectionDialogFragment
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.background.roundBackground
import com.foryouandme.core.entity.configuration.button.button
import com.foryouandme.core.entity.consent.review.ConsentReview
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.core.ext.startCoroutineAsync
import kotlinx.android.synthetic.main.consent_review_disagree.*

class ConsentReviewDisagreeFragment : ConsentReviewSectionDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.consent_review_disagree, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        consentReviewAndConfiguration { config, state ->

            setupView()
            applyConfiguration(config, state.consentReview)

        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            close_recording.setImageResource(imageConfiguration.close())
            close_recording.setOnClickListener {
                startCoroutineAsync {
                    viewModel.back(
                        consentReviewNavController(),
                        consentNavController(),
                        onboardingStepNavController(),
                        authNavController(),
                        rootNavController()
                    )
                }
            }

            disagree.setOnClickListener {
                startCoroutineAsync { viewModel.exit(rootNavController()) }
            }

        }

    private suspend fun applyConfiguration(
        configuration: Configuration,
        consentReview: ConsentReview
    ): Unit =
        evalOnMain {

            root.background =
                roundBackground(configuration.theme.secondaryColor.color())

            body.text = consentReview.disagreeModalBody
            body.setTextColor(configuration.theme.primaryTextColor.color())

            disagree.text = consentReview.disagreeModalButton
            disagree.setTextColor(configuration.theme.secondaryColor.color())
            disagree.background =
                button(configuration.theme.primaryColorEnd.color())

        }
}