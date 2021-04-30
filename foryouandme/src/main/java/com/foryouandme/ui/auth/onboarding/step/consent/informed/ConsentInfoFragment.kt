package com.foryouandme.ui.auth.onboarding.step.consent.informed

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.AnywhereToWelcome
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.databinding.ConsentInfoBinding
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentSectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentInfoFragment : ConsentSectionFragment(R.layout.consent_info) {

    private val viewModel: ConsentInfoViewModel by viewModels()

    val binding: ConsentInfoBinding?
        get() = view?.let { ConsentInfoBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ConsentInfoStateUpdate.ConsentInfo -> setupNavigation()
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    ConsentInfoLoading.ConsentInfo ->
                        binding?.loading?.setVisibility(it.active, false)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    ConsentInfoError.ConsentInfo ->
                        binding?.error?.setError(it.error, configuration) { initialize() }
                }
            }
            .observeIn(this)

        viewModel.navigation
            .unwrapEvent(name)
            .onEach {
                navigator.navigateTo(consentInfoNavController(), it)
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        initialize()
    }

    private fun initialize() {
        val configuration = configuration
        if (viewModel.state.consentInfo == null && configuration != null)
            viewModel.execute(ConsentInfoStateEvent.GetConsentInfo(configuration))
        else
            setupNavigation()
    }

    private fun setupNavigation() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.consent_info_navigation)
            navHostFragment.navController.graph = graph
        }

    }

    fun showAbort(color: Int, type: ConsentInfoAbort) {

        val viewBinding = binding
        val configuration = configuration

        if (viewBinding != null && configuration != null) {

            viewBinding.abort.text = configuration.text.onboarding.abortButton
            viewBinding.abort.setTextColor(color)
            viewBinding.abort.setOnClickListener { showAbortAlert(type) }
            viewBinding.abort.isVisible = true

        }

    }

    fun hideAbort() {

        binding?.abort?.isVisible = false

    }

    private fun showAbortAlert(abort: ConsentInfoAbort) {

        val configuration = configuration

        if (configuration != null) {
            AlertDialog.Builder(requireContext())
                .setTitle(configuration.text.onboarding.abortTitle)
                .setMessage(configuration.text.onboarding.abortMessage)
                .setPositiveButton(configuration.text.onboarding.abortConfirm)
                { _, _ ->
                    viewModel.execute(ConsentInfoStateEvent.Abort(abort))
                    navigator.navigateTo(authNavController(), AnywhereToWelcome)
                }
                .setNegativeButton(configuration.text.onboarding.abortCancel, null)
                .show()
        }

    }

    private fun consentInfoNavController(): ConsentInfoNavController =
        ConsentInfoNavController(childFragmentManager.fragments[0].findNavController())

}