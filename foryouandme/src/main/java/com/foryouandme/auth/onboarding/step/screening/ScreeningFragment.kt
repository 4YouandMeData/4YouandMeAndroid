package com.foryouandme.auth.onboarding.step.screening

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.auth.onboarding.step.OnboardingStepFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync
import kotlinx.android.synthetic.main.screening.*

class ScreeningFragment : OnboardingStepFragment<ScreeningViewModel>(R.layout.screening) {

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                ScreeningViewModel(
                    navigator,
                    injector.screeningModule(),
                    injector.answerModule(),
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
                    is ScreeningStateUpdate.Initialization ->
                        startCoroutineAsync { setupNavigation() }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    ScreeningLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) { payload ->
                when (payload.cause) {
                    ScreeningError.Initialization ->
                        error.setError(payload.error)
                        { view ->
                            view.hide()
                            configuration { viewModel.initialize(rootNavController(), it) }
                        }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {

            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController(), it)

        }
    }

    private fun setupNavigation(): Unit {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.screening_navigation)
        navHostFragment.navController.graph = graph

    }

    suspend fun showAbort(configuration: Configuration, color: Int): Unit =
        evalOnMain {

            abort.text = configuration.text.onboarding.abortButton
            abort.setTextColor(color)
            abort.setOnClickListener { startCoroutineAsync { showAbortAlert(configuration) } }
            abort.isVisible = true

        }

    suspend fun hideAbort(): Unit =
        evalOnMain {

            abort.isVisible = false

        }

    private suspend fun showAbortAlert(configuration: Configuration): Unit =
        evalOnMain {

            AlertDialog.Builder(requireContext())
                .setTitle(configuration.text.onboarding.abortTitle)
                .setMessage(configuration.text.onboarding.abortMessage)
                .setPositiveButton(configuration.text.onboarding.abortConfirm)
                { _, _ ->
                    startCoroutineAsync { viewModel.abort(authNavController()) }
                }
                .setNegativeButton(configuration.text.onboarding.abortCancel, null)
                .show()

        }
}