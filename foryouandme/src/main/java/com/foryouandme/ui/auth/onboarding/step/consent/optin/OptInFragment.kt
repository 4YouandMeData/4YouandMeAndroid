package com.foryouandme.ui.auth.onboarding.step.consent.optin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.databinding.OptInBinding
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentSectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OptInFragment : ConsentSectionFragment(R.layout.opt_in) {

    private val viewModel: OptInViewModel by viewModels()

    val binding: OptInBinding?
        get() = view?.let { OptInBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is OptInStateUpdate.OptIn -> setupNavigation()
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    OptInLoading.Permission ->
                        binding?.loading?.setVisibility(it.active, false)
                    else ->
                        Unit
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    OptInError.OptIn ->
                        binding?.error?.setError(it.error, configuration)
                        { viewModel.execute(OptInStateEvent.GetOptIn) }
                    else -> Unit
                }
            }

        viewModel.navigation
            .unwrapEvent(name)
            .onEach { navigator.navigateTo(optInNavController(), it) }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.state.optIns == null)
            viewModel.execute(OptInStateEvent.GetOptIn)
        else
            setupNavigation()

    }

    private fun setupNavigation() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.opt_in_navigation)
            navHostFragment.navController.graph = graph
        }
    }

    private fun optInNavController(): OptInNavController =
        OptInNavController(childFragmentManager.fragments[0].findNavController())

}