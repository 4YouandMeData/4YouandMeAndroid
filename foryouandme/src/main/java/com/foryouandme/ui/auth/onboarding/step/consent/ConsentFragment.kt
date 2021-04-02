package com.foryouandme.ui.auth.onboarding.step.consent

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.core.ext.navigator
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepFragmentOld
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConsentFragment private constructor(
) : OnboardingStepFragmentOld<ConsentViewModel>(R.layout.consent) {

    override val viewModel: ConsentViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { ConsentViewModel(navigator) }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigation()

    }

    private fun setupNavigation() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.consent_navigation)
            graph.startDestination = if (onlyOptInArg()) R.id.opt_in else R.id.consent_info
            navHostFragment.navController.graph = graph
        }

    }

    fun onlyOptInArg(): Boolean = arguments?.getBoolean(ONLY_OPT_IN, false)!!

    companion object {

        private const val ONLY_OPT_IN = "opt_in"

        fun build(onlyOptIn: Boolean): ConsentFragment =
            ConsentFragment().apply {
                val bundle = Bundle().apply { putBoolean(ONLY_OPT_IN, onlyOptIn) }
                arguments = bundle

            }

    }

}