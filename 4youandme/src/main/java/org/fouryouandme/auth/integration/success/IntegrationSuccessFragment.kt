package org.fouryouandme.auth.integration.success

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
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

class IntegrationSuccessFragment : BaseFragment<IntegrationViewModel>(R.layout.integration_page) {

    override val viewModel: IntegrationViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { IntegrationViewModel(navigator, IORuntime) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Option.fx { !viewModel.state().configuration to !viewModel.state().integration }
            .map { applyData(it.first, it.second) }

        setupView()

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

        page.applyData(
            configuration,
            integration.successPage,
            { viewModel.openMain(rootNavController()) },
            { },
            { _, _ -> }
        )

    }

}