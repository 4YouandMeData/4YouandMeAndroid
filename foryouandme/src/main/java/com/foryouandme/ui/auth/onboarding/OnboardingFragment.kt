package com.foryouandme.ui.auth.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.databinding.OnboardingBinding
import com.foryouandme.ui.auth.AuthSectionFragment
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepFragment
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OnboardingFragment : AuthSectionFragment(R.layout.onboarding) {

    private val viewModel: OnboardingViewModel by viewModels()

    private val binding: OnboardingBinding?
        get() = view?.let { OnboardingBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    OnboardingLoading.Initialization,
                    OnboardingLoading.NextStep ->
                        binding?.loading?.setVisibility(it.active)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    OnboardingError.Initialization ->
                        binding?.error?.setError(it.error, viewModel.state.configuration)
                        { viewModel.execute(OnboardingStateEvent.Initialize) }
                    OnboardingError.NextStep ->
                        binding?.error?.setError(it.error, viewModel.state.configuration)
                }
            }
            .observeIn(this)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is OnboardingStateUpdate.Initialized -> setupNavigation()
                }
            }
            .observeIn(this)

        viewModel.navigation
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    OnboardingToMain ->
                        navigator.navigateTo(rootNavController(), it)
                    is OnboardingStepToOnboardingStep ->
                        navigator.navigateTo(onboardingNavController(), it)
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isInitialized().not())
            viewModel.execute(OnboardingStateEvent.Initialize)
        else
            setupNavigation()

    }

    private fun setupNavigation() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.onboarding_navigation)
            navHostFragment.navController.graph = graph
        }

    }

    private fun onboardingNavController(): OnboardingStepNavController {

        val stepFragment = childFragmentManager.fragments[0]
            .childFragmentManager.fragments[0]
            .childFragmentManager.fragments[0]

        return (stepFragment as OnboardingStepFragment).onboardingStepNavController()

    }

}