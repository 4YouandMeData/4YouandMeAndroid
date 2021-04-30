package com.foryouandme.ui.auth.onboarding.step.consent.review

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.databinding.ConsentReviewBinding
import com.foryouandme.ui.auth.onboarding.step.consent.ConsentSectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentReviewFragment : ConsentSectionFragment(
    R.layout.consent_review
) {

    private val viewModel: ConsentReviewViewModel by viewModels()

    private val binding: ConsentReviewBinding?
        get() = view?.let { ConsentReviewBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ConsentReviewStateUpdate.ConsentReview -> setupNavigation()
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    ConsentReviewLoading.ConsentReview ->
                        binding?.loading?.setVisibility(it.active, false)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {
                when (it.cause) {
                    ConsentReviewError.ConsentReview ->
                        binding?.error?.setError(it.error, configuration) { getConsentReview() }
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.state.consentReview != null) getConsentReview()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        getConsentReview()
    }

    private fun getConsentReview() {
        configuration?.let {
            viewModel.execute(ConsentReviewStateEvent.GetConsentReview(it))
        }
    }

    private fun setupNavigation() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.consent_review_navigation)
            navHostFragment.navController.graph = graph
        }

    }

}