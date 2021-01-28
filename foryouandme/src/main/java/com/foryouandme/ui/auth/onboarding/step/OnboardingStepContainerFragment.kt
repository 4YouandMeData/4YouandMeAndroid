package com.foryouandme.ui.auth.onboarding.step

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.OnboardingFragment
import com.foryouandme.ui.auth.onboarding.OnboardingViewModel
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.core.ext.navigator

class OnboardingStepContainerFragment : BaseFragmentOld<OnboardingViewModel>(
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
                transaction.add(R.id.onboarding_step, it.b, it.a.identifier)
                transaction.commit()

            }
    }

    fun onboardingFragment(): OnboardingFragment = find()

    fun authNavController(): AuthNavController = onboardingFragment().authNavController()

}