package org.fouryouandme.auth.optin.success

import android.os.Bundle
import android.view.View
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import kotlinx.android.synthetic.main.opt_in.*
import kotlinx.android.synthetic.main.opt_in_success.*
import org.fouryouandme.R
import org.fouryouandme.auth.optin.OptInViewModel
import org.fouryouandme.auth.screening.ScreeningFragment
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.optins.OptIns
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.removeBackButton
import org.fouryouandme.core.ext.setStatusBar
import org.fouryouandme.core.view.page.EPageType

class OptInSuccessFragment : BaseFragment<OptInViewModel>(R.layout.opt_in_success) {

    override val viewModel: OptInViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { OptInViewModel(navigator, IORuntime) })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().optIns }
            .map { applyData(it.first, it.second) }
    }

    private fun setupView() {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map { it.removeBackButton() }

        (requireParentFragment().requireParentFragment() as? ScreeningFragment)
            .toOption()
            .map { it.hideAbort() }

    }

    private fun applyData(configuration: Configuration, optIns: OptIns): Unit {

        setStatusBar(configuration.theme.secondaryColor.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.applyData(
            configuration = configuration,
            page = optIns.successPage,
            pageType = EPageType.SUCCESS,
            action1 = { viewModel.consentUser(rootNavController()) },
            externalAction = { viewModel.web(rootNavController(), it) }
        )

    }
}