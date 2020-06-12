package org.fouryouandme.auth.consent.question

import android.os.Bundle
import android.view.View
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import kotlinx.android.synthetic.main.consent.*
import kotlinx.android.synthetic.main.consent_question.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.ConsentViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.consent.Consent
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.removeBackButton

class ConsentQuestionFragment : BaseFragment<ConsentViewModel>(R.layout.consent_question) {

    override val viewModel: ConsentViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ConsentViewModel(navigator, IORuntime) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().consent }
            .map { applyData(it.first, it.second) }
    }

    fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map { it.removeBackButton() }

    }

    private fun applyData(configuration: Configuration, consent: Consent): Unit {

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

    }

}