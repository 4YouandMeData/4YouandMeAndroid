package org.fouryouandme.auth.consent.user.success

import android.os.Bundle
import android.view.View
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import kotlinx.android.synthetic.main.consent_user.*
import kotlinx.android.synthetic.main.consent_user_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.user.ConsentUserViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.user.ConsentUser
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.removeBackButton
import org.fouryouandme.core.view.page.EPageType

class ConsentUserSuccessFragment : BaseFragment<ConsentUserViewModel>(R.layout.consent_user_page) {

    override val viewModel: ConsentUserViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ConsentUserViewModel(navigator, IORuntime) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().consent to !viewModel.state().configuration }
            .map { applyData(it.second, it.first) }
    }

    private fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map { it.removeBackButton() }
    }

    private fun applyData(configuration: Configuration, consentUser: ConsentUser): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.applyData(
            configuration = configuration,
            page = consentUser.successPage,
            pageType = EPageType.SUCCESS,
            action1 = {},
            externalAction = { viewModel.web(rootNavController(), it) }
        )

    }
}