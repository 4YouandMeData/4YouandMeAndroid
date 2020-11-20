package com.foryouandme.auth.onboarding.step.consent.review

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

class ConsentReviewFragment : ConsentSectionFragment<ConsentReviewViewModel>(
    R.layout.consent_review
) {

    override val viewModel: ConsentReviewViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                ConsentReviewViewModel(
                    navigator,
                    injector.consentReviewModule(),
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
                    is ConsentReviewStateUpdate.Initialization ->
                        startCoroutineAsync { setupNavigation() }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    ConsentReviewLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) { payload ->
                when (payload.cause) {
                    ConsentReviewError.Initialization ->
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
        val graph = inflater.inflate(R.navigation.consent_review_navigation)
        navHostFragment.navController.graph = graph

    }

}