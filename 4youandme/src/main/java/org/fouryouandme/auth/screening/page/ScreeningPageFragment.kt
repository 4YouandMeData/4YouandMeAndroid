package org.fouryouandme.auth.screening.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.extensions.list.foldable.firstOrNone
import arrow.core.toOption
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_page.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.informed.ConsentInfoFragment
import org.fouryouandme.auth.screening.ScreeningViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.*
import org.fouryouandme.core.view.page.EPageType

class ScreeningPageFragment : BaseFragment<ScreeningViewModel>(R.layout.screening_page) {

    private val args: ScreeningPageFragmentArgs by navArgs()

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ScreeningViewModel(navigator, IORuntime) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().screening }
            .map { applyData(it.first, it.second) }

    }

    private fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map {

                it.showBackSecondaryButton(imageConfiguration)
                { viewModel.back(findNavController()) }

            }

    }

    private fun applyData(configuration: Configuration, screening: Screening): Unit {

        setStatusBar(configuration.theme.secondaryColor.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        (requireParentFragment().requireParentFragment() as? ConsentInfoFragment)
            .toOption()
            .map { it.showAbort(configuration, configuration.theme.primaryColorEnd.color()) }

        screening.pages.firstOrNone { it.id == args.id }
            .map { data ->
                page.applyData(
                    configuration,
                    data,
                    EPageType.INFO,
                    { option ->
                        option.fold(
                            { viewModel.questions(findNavController(), false) },
                            { viewModel.page(findNavController(), it.id, false) })
                    },
                    {},
                    { viewModel.web(rootNavController(), it) }
                )
            }

    }
}