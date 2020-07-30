package org.fouryouandme.auth.integration.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.firstOrNone
import arrow.core.toOption
import kotlinx.android.synthetic.main.integration.*
import kotlinx.android.synthetic.main.integration_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.integration.IntegrationViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.integration.Integration
import org.fouryouandme.core.ext.*

class IntegrationPageFragment : BaseFragment<IntegrationViewModel>(R.layout.integration_page) {

    private val args: IntegrationPageFragmentArgs by navArgs()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().integration }
            .map { applyData(it.first, it.second) }
    }

    private fun setupView(): Unit {

        requireParentFragment().requireParentFragment().toolbar
            .toOption()
            .map {
                it.showBackSecondaryButton(imageConfiguration)
                { viewModel.back(findNavController()) }
            }
    }

    private fun applyData(configuration: Configuration, integration: Integration): Unit {

        setStatusBar(configuration.theme.secondaryColor.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        integration.pages.firstOrNone { it.id == args.id }
            .map { integrationPage ->
                page.applyData(
                    configuration,
                    integrationPage,
                    { viewModel.nextPage(findNavController(), it, false) },
                    { viewModel.handleSpecialLink(it) },
                    { url, nextPage ->
                        viewModel.login(findNavController(), url, nextPage, false)
                    }
                )
            }

    }
}