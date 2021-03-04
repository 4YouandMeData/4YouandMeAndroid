package com.foryouandme.ui.auth.onboarding

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.ui.auth.AuthSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.navigator

class OnboardingFragment : AuthSectionFragment<OnboardingViewModel>(R.layout.onboarding) {

    override val viewModel: OnboardingViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { OnboardingViewModel(navigator) }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {

            if (viewModel.isInitialized().not())
                viewModel.initialize(it)

            setupNavigation()

        }

    }

    private suspend fun setupNavigation(): Unit =
        evalOnMain {

            val navHostFragment =
                childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val currentGraph = catchToNull { navHostFragment.navController.graph }
            if (currentGraph == null) {
                val inflater = navHostFragment.navController.navInflater
                val graph = inflater.inflate(R.navigation.onboarding_navigation)
                navHostFragment.navController.graph = graph
            }

        }

}