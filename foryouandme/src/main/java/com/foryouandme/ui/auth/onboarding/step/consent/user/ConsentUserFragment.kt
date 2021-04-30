package com.foryouandme.ui.auth.onboarding.step.consent.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.databinding.ConsentUserBinding
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.optin.OptInNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentUserFragment : ConsentSectionFragment(R.layout.consent_user) {

    val viewModel: ConsentUserViewModel by viewModels()

    val binding: ConsentUserBinding?
        get() = view?.let { ConsentUserBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ConsentUserStateUpdate.GetConsentUser -> setupNavigation()
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    ConsentUserLoading.GetConsentUser ->
                        binding?.loading?.setVisibility(it.active, false)
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    ConsentUserError.GetConsentUser ->
                        binding?.error?.setError(it.error, configuration)
                        { viewModel.execute(ConsentUserAction.GetConsentUser) }
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.navigation
            .unwrapEvent(name)
            .onEach { navigator.navigateTo(consentUserNavController(), it) }
            .observeIn(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.state.consent == null)
            viewModel.execute(ConsentUserAction.GetConsentUser)
        else
            setupNavigation()

    }

    private fun setupNavigation() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.consent_user_navigation)
            navHostFragment.navController.graph = graph
        }

    }

    private fun consentUserNavController(): ConsentUserNavController =
        ConsentUserNavController(childFragmentManager.fragments[0].findNavController())

}