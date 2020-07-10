package org.fouryouandme.auth.screening.failure

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.screening.ScreeningFragment
import org.fouryouandme.auth.screening.ScreeningViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.removeBackButton
import org.fouryouandme.core.ext.setStatusBar
import org.fouryouandme.core.view.page.EPageType

class ScreeningFailureFragment : BaseFragment<ScreeningViewModel>(R.layout.screening_page) {

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ScreeningViewModel(navigator, IORuntime) })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().screening }
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

    private fun applyData(configuration: Configuration, screening: Screening): Unit {

        setStatusBar(configuration.theme.secondaryColor.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.applyData(
            configuration = configuration,
            page = screening.failurePage,
            pageType = EPageType.FAILURE,
            action1 = { viewModel.retryFromWelcome(findNavController()) },
            externalAction = { viewModel.web(rootNavController(), it) }
        )
    }
}