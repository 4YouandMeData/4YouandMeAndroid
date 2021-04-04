package com.foryouandme.ui.auth.onboarding.step.consent.review

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.BaseDialogFragment
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.find
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentNavController
import kotlinx.coroutines.flow.onEach

abstract class ConsentReviewSectionDialogFragment : BaseDialogFragment() {

    val viewModel: ConsentReviewViewModel
            by viewModels(ownerProducer = { consentReviewFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when(it) {
                    ConsentReviewStateUpdate.ConsentReview -> onConsentReviewUpdate()

                }
            }
            .observeIn(this)

    }

    open fun onConsentReviewUpdate() {

    }

    fun consentReviewFragment(): ConsentReviewFragment = find()

    fun authNavController(): AuthNavController = consentReviewFragment().authNavController()

    fun onboardingStepNavController(): OnboardingStepNavController =
        consentReviewFragment().onboardingStepNavController()

    fun consentNavController(): ConsentNavController =
        consentReviewFragment().consentNavController()

    fun consentReviewNavController(): ConsentReviewNavController =
        ConsentReviewNavController(findNavController())

    fun back(): Boolean =
        if (navigator.back(consentReviewNavController()).not())
            if (navigator.back(consentNavController()).not())
                if (navigator.back(onboardingStepNavController()).not())
                    if (navigator.back(authNavController()).not())
                        navigator.back(rootNavController())
                    else true
                else true
            else true
        else true

}