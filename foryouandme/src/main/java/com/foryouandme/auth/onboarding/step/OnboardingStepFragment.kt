package com.foryouandme.auth.onboarding.step

import android.os.Bundle
import com.foryouandme.R
import com.foryouandme.auth.onboarding.OnboardingFragment
import com.foryouandme.auth.onboarding.OnboardingViewModel
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.navigator

abstract class OnboardingStepFragment : BaseFragment<OnboardingViewModel>(
    R.layout.onboarding_step
) {

    override val viewModel: OnboardingViewModel by lazy {

        viewModelFactory(
            onboardingFragment(),
            getFactory { OnboardingViewModel(navigator) }
        )

    }

    fun onboardingFragment(): OnboardingFragment = find()

    protected fun indexArg(): Int =
        arguments?.getInt(INDEX, -1)
            ?.let { if (it == -1) null else it }!!

    companion object {

        private const val INDEX = "index"

        fun <T : OnboardingStepFragment> buildWithParams(index: Int, fragment: T): T {

            val bundle = Bundle()
            bundle.putInt(INDEX, index)
            fragment.arguments = bundle

            return fragment

        }

    }

}