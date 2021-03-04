package com.foryouandme.ui.auth.onboarding.step.consent.optin

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.*
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentSectionFragment
import kotlinx.android.synthetic.main.screening.*

class OptInFragment : ConsentSectionFragment<OptInViewModel>(R.layout.opt_in) {

    override val viewModel: OptInViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                OptInViewModel(
                    navigator,
                    injector.optInModule(),
                    injector.permissionModule()
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) { update ->
                when (update) {
                    is OptInStateUpdate.Initialization ->
                        startCoroutineAsync { setupNavigation() }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    OptInLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) { payload ->
                when (payload.cause) {
                    OptInError.Initialization ->
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

    private suspend fun setupNavigation() {
        evalOnMain {
            val navHostFragment =
                childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val currentGraph = catchToNull { navHostFragment.navController.graph }
            if (currentGraph == null) {
                val inflater = navHostFragment.navController.navInflater
                val graph = inflater.inflate(R.navigation.opt_in_navigation)
                navHostFragment.navController.graph = graph
            }

        }
    }

}