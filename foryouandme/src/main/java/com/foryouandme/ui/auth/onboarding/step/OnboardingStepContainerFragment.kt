package com.foryouandme.ui.auth.onboarding.step

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.OnboardingFragment
import com.foryouandme.ui.auth.onboarding.OnboardingViewModel
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.mapNotNull
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
            step?.let { it.view() }?.let { OnboardingStepFragmentOld.buildWithParams(args.index, it) }

        mapNotNull(step, fragment)
            ?.let {

                val transaction = childFragmentManager.beginTransaction()
                transaction.add(R.id.onboarding_step, it.b, it.a.identifier)
                transaction.commit()

            }
    }

    fun onboardingFragment(): OnboardingFragment = find()

    fun authNavController(): AuthNavController = onboardingFragment().authNavController()

}