package org.fouryouandme.auth.screening.success

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
import kotlinx.android.synthetic.main.screening_response.*
import org.fouryouandme.R
import org.fouryouandme.auth.screening.ScreeningViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class ScreeningSuccessFragment : BaseFragment<ScreeningViewModel>(R.layout.screening_response) {

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ScreeningViewModel(navigator, IORuntime) })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Option.fx { !viewModel.state().configuration to !viewModel.state().screening }
            .map { applyData(it.first, it.second) }
    }

    private fun applyData(configuration: Configuration, screening: Screening): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.applyData(
            configuration,
            null,
            true,
            screening.successPage
        ) { viewModel.consent(rootNavController()) }

    }
}