package com.foryouandme.ui.auth.onboarding.step.screening

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
import com.foryouandme.databinding.ScreeningBinding
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ScreeningFragment : OnboardingStepFragment(R.layout.screening) {

    private val viewModel: ScreeningViewModel by viewModels()

    val binding: ScreeningBinding?
        get() = view?.let { ScreeningBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ScreeningStateUpdate.Screening -> setupNavigation()
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    ScreeningLoading.Screening ->
                        binding?.loading?.setVisibility(it.active, false)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    ScreeningError.Screening ->
                        binding?.error?.setError(it.error, configuration) { setUpView() }
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()

        setUpView()

    }

    private fun setUpView() {

        val config = configuration

        if (viewModel.state.screening == null && config != null)
            viewModel.execute(ScreeningStateEvent.GetScreening(config))

    }

    private fun setupNavigation() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.screening_navigation)
            navHostFragment.navController.graph = graph
        }

    }

    fun showAbort(color: Int) {

        val viewBinding = binding
        val config = configuration

        if (viewBinding != null && config != null) {
            viewBinding.abort.text = config.text.onboarding.abortButton
            viewBinding.abort.setTextColor(color)
            viewBinding.abort.setOnClickListener { showAbortAlert() }
            viewBinding.abort.isVisible = true
        }

    }

    fun hideAbort() {

        binding?.abort?.isVisible = false

    }

    private fun showAbortAlert() {

        val config = configuration

        if (config != null) {

            AlertDialog.Builder(requireContext())
                .setTitle(config.text.onboarding.abortTitle)
                .setMessage(config.text.onboarding.abortMessage)
                .setPositiveButton(config.text.onboarding.abortConfirm)
                { _, _ ->
                    viewModel.execute(ScreeningStateEvent.Abort)
                    navigator.navigateTo(rootNavController(), AnywhereToWelcome)
                }
                .setNegativeButton(config.text.onboarding.abortCancel, null)
                .show()

        }

    }

    private fun screeningNavController(): ScreeningNavController =
        ScreeningNavController(childFragmentManager.fragments[0].findNavController())

}