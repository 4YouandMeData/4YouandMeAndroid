package com.foryouandme.ui.auth.onboarding.step.consent.informed

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.ext.*
import kotlinx.android.synthetic.main.consent_info.*

class ConsentInfoFragment : ConsentSectionFragment<ConsentInfoViewModel>(R.layout.consent_info) {

    override val viewModel: ConsentInfoViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                ConsentInfoViewModel(
                    navigator,
                    injector.consentInfoModule(),
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
                    is ConsentInfoStateUpdate.Initialization ->
                        startCoroutineAsync { setupNavigation() }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    ConsentInfoLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) { payload ->
                when (payload.cause) {
                    ConsentInfoError.Initialization ->
                        error.setError(payload.error)
                        {
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
        val graph = inflater.inflate(R.navigation.consent_info_navigation)
        navHostFragment.navController.graph = graph

    }

    suspend fun showAbort(configuration: Configuration, color: Int, type: ConsentInfoAbort): Unit =
        evalOnMain {

            abort.text = configuration.text.onboarding.abortButton
            abort.setTextColor(color)
            abort.setOnClickListenerAsync { showAbortAlert(configuration, type) }
            abort.isVisible = true

        }

    suspend fun hideAbort(): Unit =
        evalOnMain {

            abort.isVisible = false

        }

    private suspend fun showAbortAlert(
        configuration: Configuration,
        abort: ConsentInfoAbort
    ): AlertDialog =
        evalOnMain {

            AlertDialog.Builder(requireContext())
                .setTitle(configuration.text.onboarding.abortTitle)
                .setMessage(configuration.text.onboarding.abortMessage)
                .setPositiveButton(configuration.text.onboarding.abortConfirm)
                { _, _ ->
                    startCoroutineAsync { viewModel.abort(authNavController(), abort) }
                }
                .setNegativeButton(configuration.text.onboarding.abortCancel, null)
                .show()

        }

}