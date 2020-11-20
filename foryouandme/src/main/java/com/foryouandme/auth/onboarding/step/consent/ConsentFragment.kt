package com.foryouandme.auth.onboarding.step.consent

import com.foryouandme.R
import com.foryouandme.auth.onboarding.step.OnboardingStepFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.navigator

class ConsentFragment : OnboardingStepFragment<ConsentViewModel>(R.layout.consent) {

    override val viewModel: ConsentViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { ConsentViewModel(navigator) }
        )

    }
}