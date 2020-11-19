package com.foryouandme.auth.onboarding

import com.foryouandme.R
import com.foryouandme.auth.AuthSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.navigator

class OnboardingFragment : AuthSectionFragment<OnboardingViewModel>(R.layout.onboarding) {

    override val viewModel: OnboardingViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { OnboardingViewModel(navigator) }
        )

    }
}