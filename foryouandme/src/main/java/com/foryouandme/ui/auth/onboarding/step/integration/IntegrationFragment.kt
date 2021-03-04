package com.foryouandme.ui.auth.onboarding.step.integration

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.*
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.screening.*

@AndroidEntryPoint
class IntegrationFragment : OnboardingStepFragment<IntegrationViewModel>(R.layout.integration) {

    override val viewModel: IntegrationViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                IntegrationViewModel(
                    navigator,
                    injector.authModule(),
                    injector.integrationModule(),
                    injector.analyticsModule()
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) { update ->
                when (update) {
                    is IntegrationStateUpdate.Initialization ->
                        startCoroutineAsync { setupNavigation() }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    IntegrationLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) { payload ->
                when (payload.cause) {
                    IntegrationError.Initialization ->
                        error.setError(payload.error)
                        {
                            startCoroutineAsync { viewModel.initialize(rootNavController()) }
                        }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController())

        }
    }

    private suspend fun setupNavigation(): Unit {
        evalOnMain {
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

}