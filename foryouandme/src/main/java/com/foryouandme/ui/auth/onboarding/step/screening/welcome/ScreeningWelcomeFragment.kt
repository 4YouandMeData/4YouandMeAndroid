package com.foryouandme.ui.auth.onboarding.step.screening.welcome

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.ext.hide
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.databinding.ScreeningWelcomeBinding
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningSectionFragment
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningStateUpdate
import com.foryouandme.ui.web.EWebPageType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ScreeningWelcomeFragment : ScreeningSectionFragment(R.layout.screening_welcome) {

    private val binding: ScreeningWelcomeBinding?
        get() = view?.let { ScreeningWelcomeBinding.bind(it) }

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

        screeningFragment().binding?.toolbar?.hide()

    }

    private fun applyData() {

        val viewBinding = binding
        val config = configuration
        val screening = viewModel.state.screening

        if (viewBinding != null && config != null && screening != null) {

            setStatusBar(config.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(config.theme.secondaryColor.color())

            viewBinding.page.isVisible = true
            viewBinding.page.applyData(
                configuration = config,
                page = screening.welcomePage,
                pageType = EPageType.INFO,
                action1 = {
                    if (it == null) questions(true)
                    else page(it.id, true)
                },
                extraStringAction = {
                    navigator.navigateTo(
                        rootNavController(),
                        AnywhereToWeb(
                            it,
                            EWebPageType.LEARN_MORE
                        )
                    )
                }
            )

            screeningFragment().hideAbort()

        }
    }

}