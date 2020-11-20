package com.foryouandme.auth.onboarding.step.consent.user

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.auth.AuthSectionFragment
import com.foryouandme.auth.onboarding.step.consent.ConsentSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync
import kotlinx.android.synthetic.main.screening.*

class ConsentUserFragment : ConsentSectionFragment<ConsentUserViewModel>(R.layout.consent_user) {

    override val viewModel: ConsentUserViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                ConsentUserViewModel(
                    navigator,
                    injector.consentUserModule(),
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
                    is ConsentUserStateUpdate.Initialization ->
                        startCoroutineAsync { setupNavigation() }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    ConsentUserLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) { payload ->
                when (payload.cause) {
                    ConsentUserError.Initialization ->
                        error.setError(payload.error)
                        { view ->
                            view.hide()
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

    private fun setupNavigation(): Unit {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.consent_user_navigation)
        navHostFragment.navController.graph = graph

    }

}