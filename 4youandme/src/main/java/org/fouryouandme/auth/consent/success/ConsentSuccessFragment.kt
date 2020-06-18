package org.fouryouandme.auth.consent.success

import android.os.Bundle
import android.view.View
import arrow.core.Option
import arrow.core.extensions.fx
import kotlinx.android.synthetic.main.consent_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.ConsentViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.Consent
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class ConsentSuccessFragment : BaseFragment<ConsentViewModel>(R.layout.consent_page) {

    override val viewModel: ConsentViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ConsentViewModel(navigator, IORuntime) })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Option.fx { !viewModel.state().configuration to !viewModel.state().consent }
            .map { applyData(it.first, it.second) }
    }

    private fun applyData(configuration: Configuration, consent: Consent): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.applyData(
            configuration,
            null,
            true,
            consent.successPage,
            { },
            { viewModel.web(rootNavController(), it) }
        )

    }
}