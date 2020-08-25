package org.fouryouandme.auth.integration.welcome

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import kotlinx.android.synthetic.main.integration.*
import kotlinx.android.synthetic.main.integration_welcome.*
import org.fouryouandme.R
import org.fouryouandme.auth.integration.IntegrationError
import org.fouryouandme.auth.integration.IntegrationLoading
import org.fouryouandme.auth.integration.IntegrationStateUpdate
import org.fouryouandme.auth.integration.IntegrationViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.integration.Integration
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.removeBackButton
import org.fouryouandme.core.ext.setStatusBar

class IntegrationWelcomeFragment :
    BaseFragment<IntegrationViewModel>(R.layout.integration_welcome) {

    override val viewModel: IntegrationViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                IntegrationViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(IntegrationWelcomeFragment::class.java.simpleName) {
                when (it) {
                    is IntegrationStateUpdate.Initialization ->
                        applyData(it.configuration, it.integration)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(IntegrationWelcomeFragment::class.java.simpleName) {
                when (it.task) {
                    IntegrationLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(IntegrationWelcomeFragment::class.java.simpleName) {
                when (it.cause) {
                    IntegrationError.Initialization ->
                        error.setError(it.error) { viewModel.initialize(rootNavController()) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().integration }
            .fold({ viewModel.initialize(rootNavController()) }, { applyData(it.first, it.second) })
    }

    private fun setupView(): Unit {

        requireParentFragment().requireParentFragment().toolbar
            .toOption()
            .map { it.removeBackButton() }
    }

    private fun applyData(configuration: Configuration, integration: Integration): Unit {

        setStatusBar(configuration.theme.secondaryColor.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.applyData(
            configuration,
            integration.welcomePage,
            { viewModel.nextPage(findNavController(), it, true) },
            { viewModel.handleSpecialLink(it) },
            { url, nextPage ->
                viewModel.login(findNavController(), url, nextPage, true)
            }
        )

    }
}