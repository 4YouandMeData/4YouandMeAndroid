package org.fouryouandme.auth.screening.welcome

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
import kotlinx.android.synthetic.main.screening_welcome.*
import org.fouryouandme.R
import org.fouryouandme.auth.screening.ScreeningError
import org.fouryouandme.auth.screening.ScreeningStateUpdate
import org.fouryouandme.auth.screening.ScreeningViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.showBackSecondaryButton

class ScreeningWelcomeFragment : BaseFragment<ScreeningViewModel>(R.layout.screening_welcome) {

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ScreeningViewModel(navigator, IORuntime) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is ScreeningStateUpdate.Initialization ->
                        applyData(it.configuration, it.screening)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent { loading.setVisibility(it.active) }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    ScreeningError.Initialization ->
                        error.setError(it.error)
                        { viewModel.initialize(rootNavController()) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().screening }
            .fold(
                {
                    viewModel.initialize(rootNavController())
                    page.isVisible = false
                },
                { applyData(it.first, it.second) }
            )
    }

    private fun setupView(): Unit {

        loading.setLoader(imageConfiguration.loading())

        toolbar.showBackSecondaryButton(imageConfiguration)
        { viewModel.back(rootNavController()) }

    }

    private fun applyData(configuration: Configuration, screening: Screening): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.isVisible = true
        page.applyData(configuration, null, false, screening.welcomePage)
        { viewModel.questions(findNavController()) }

    }
}