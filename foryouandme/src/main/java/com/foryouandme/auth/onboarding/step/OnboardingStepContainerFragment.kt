package com.foryouandme.auth.onboarding.step

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.auth.AuthNavController
import com.foryouandme.auth.onboarding.OnboardingFragment
import com.foryouandme.auth.onboarding.OnboardingViewModel
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.core.ext.navigator
import com.foryouandme.researchkit.step.StepFragment

class OnboardingStepContainerFragment : BaseFragment<OnboardingViewModel>(
    R.layout.onboarding_step
) {

    private val args: OnboardingStepContainerFragmentArgs by navArgs()

    override val viewModel: OnboardingViewModel by lazy {

        viewModelFactory(
            onboardingFragment(),
            getFactory { OnboardingViewModel(navigator) }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = viewModel.getStepByIndex(args.index)

        val fragment =
            step?.let { it.view() }?.let { OnboardingStepFragment.buildWithParams(args.index, it) }

        mapNotNull(step, fragment)
            ?.let {

                val transaction = childFragmentManager.beginTransaction()
                transaction.add(R.id.step_root, it.b, it.a.identifier)
                transaction.commit()

            }
    }

    fun onboardingFragment(): OnboardingFragment = find()

    fun authNavController(): AuthNavController = onboardingFragment().authNavController()

}