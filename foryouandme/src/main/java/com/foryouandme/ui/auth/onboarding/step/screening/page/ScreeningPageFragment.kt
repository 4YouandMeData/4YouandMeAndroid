package com.foryouandme.ui.auth.onboarding.step.screening.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.showBackSecondaryButton
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.databinding.ScreeningPageBinding
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningSectionFragment
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningStateUpdate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ScreeningPageFragment : ScreeningSectionFragment(R.layout.screening_page) {

    private val args: ScreeningPageFragmentArgs by navArgs()

    private val binding: ScreeningPageBinding?
        get() = view?.let { ScreeningPageBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ScreeningStateUpdate.Screening -> applyData()
                    else -> Unit
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        applyData()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    private fun setupView() {

        screeningFragment().binding
            ?.toolbar
            ?.showBackSecondaryButton(imageConfiguration) { back() }

    }


    private fun applyData() {


        val viewBinding = binding
        val config = configuration
        val screening = viewModel.state.screening

        if (viewBinding != null && config != null && screening != null) {

            setStatusBar(config.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(config.theme.secondaryColor.color())

            screeningFragment().showAbort(config.theme.primaryColorEnd.color())

            screening.pages.firstOrNull { it.id == args.id }
                ?.let { data ->
                    viewBinding.page.applyData(
                        config,
                        data,
                        EPageType.INFO,
                        {
                            if (it == null) questions(false)
                            else page(it.id, false)
                        },
                        {},
                        { navigator.navigateTo(rootNavController(), AnywhereToWeb(it)) }
                    )
                }

        }
    }

}