package com.foryouandme.ui.auth.onboarding.step.integration.success

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.showBackSecondaryButton
import com.foryouandme.databinding.IntegrationPageBinding
import com.foryouandme.ui.auth.onboarding.step.integration.IntegrationSectionFragment
import com.foryouandme.ui.auth.onboarding.step.integration.IntegrationStateUpdate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class IntegrationSuccessFragment : IntegrationSectionFragment(R.layout.integration_page) {

    private val binding: IntegrationPageBinding?
        get() = view?.let { IntegrationPageBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    IntegrationStateUpdate.Integration -> applyData()
                    else -> Unit
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        applyData()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    private fun setupView() {

        integrationFragment()
            .binding
            ?.toolbar
            ?.showBackSecondaryButton(imageConfiguration) { back() }

    }

    private fun applyData() {

        val viewBinding = binding
        val configuration = configuration
        val integration = viewModel.state.integration

        if (viewBinding != null && configuration != null && integration != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.page.applyData(
                configuration,
                integration.successPage,
                { integrationFragment().next() },
                { },
                { _, _ -> }
            )

        }
    }

}