package com.foryouandme.ui.auth.onboarding.step

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.ext.find
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.OnboardingFragment
import com.foryouandme.ui.auth.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingStepContainerFragment : BaseFragment(
    R.layout.onboarding_step
) {

    private val args: OnboardingStepContainerFragmentArgs by navArgs()

    val onboardingViewModel: OnboardingViewModel
            by viewModels(ownerProducer = { onboardingFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = onboardingViewModel.getStepByIndex(args.index)

        val fragment =
            step?.let { it.view() }?.let { OnboardingStepFragment.buildWithParams(args.index, it) }

        if (step != null && fragment != null) {

            val transaction = childFragmentManager.beginTransaction()
            transaction.add(R.id.onboarding_step, fragment, step.identifier)
            transaction.commit()

        }
    }

    fun onboardingFragment(): OnboardingFragment = find()

    fun authNavController(): AuthNavController = onboardingFragment().authNavController()

}