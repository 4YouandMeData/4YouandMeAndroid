package com.foryouandme.ui.auth.onboarding.step.integration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.databinding.IntegrationBinding
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class IntegrationFragment : OnboardingStepFragment(R.layout.integration) {

    private val viewModel: IntegrationViewModel by viewModels()

    val binding: IntegrationBinding?
        get() = view?.let { IntegrationBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is IntegrationStateUpdate.Integration -> setupNavigation()
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    IntegrationLoading.Integration ->
                        binding?.loading?.setVisibility(it.active, false)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    IntegrationError.Integration ->
                        binding?.error?.setError(it.error, configuration)
                        { viewModel.execute(IntegrationStateEvent.GetIntegration) }
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.state.integration == null)
            viewModel.execute(IntegrationStateEvent.GetIntegration)
        else
            setupNavigation()

    }

    private fun setupNavigation() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.integration_navigation)
            navHostFragment.navController.graph = graph
        }

    }

}