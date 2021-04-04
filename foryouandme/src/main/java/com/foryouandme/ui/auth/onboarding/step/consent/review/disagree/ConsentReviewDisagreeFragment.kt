package com.foryouandme.ui.auth.onboarding.step.consent.review.disagree

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.databinding.ConsentReviewDisagreeBinding
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.background.roundBackground
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.entity.consent.review.ConsentReview
import com.foryouandme.ui.auth.onboarding.step.consent.review.ConsentReviewDisagreeToAuth
import com.foryouandme.ui.auth.onboarding.step.consent.review.ConsentReviewSectionDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.consent_review_disagree.*

@AndroidEntryPoint
class ConsentReviewDisagreeFragment : ConsentReviewSectionDialogFragment() {

    private val binding: ConsentReviewDisagreeBinding?
        get() = view?.let { ConsentReviewDisagreeBinding.bind(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.consent_review_disagree, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        applyConfiguration()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyConfiguration()
    }

    override fun onConsentReviewUpdate() {
        super.onConsentReviewUpdate()
        applyConfiguration()
    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {

            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            close_recording.setImageResource(imageConfiguration.close())
            close_recording.setOnClickListener { back() }

            disagree.setOnClickListener {
                navigator.navigateTo(
                    rootNavController(),
                    ConsentReviewDisagreeToAuth
                )
            }

        }

    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val configuration = configuration
        val consentReview = viewModel.state.consentReview

        if (viewBinding != null && configuration != null && consentReview != null) {

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

}