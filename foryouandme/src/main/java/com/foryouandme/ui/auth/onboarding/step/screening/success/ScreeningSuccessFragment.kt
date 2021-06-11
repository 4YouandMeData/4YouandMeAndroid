package com.foryouandme.ui.auth.onboarding.step.screening.success

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.ext.removeBackButton
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.databinding.ScreeningPageBinding
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningSectionFragment
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningStateUpdate
import com.foryouandme.ui.web.EWebPageType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ScreeningSuccessFragment : ScreeningSectionFragment(R.layout.screening_page) {

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

        screeningFragment().apply {
            binding?.toolbar?.removeBackButton()
            hideAbort()
        }

    }

    private fun applyData() {

        val viewBinding = binding
        val config = configuration
        val screening = viewModel.state.screening

        if (viewBinding != null && config != null && screening != null) {

            setStatusBar(config.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(config.theme.secondaryColor.color())

            viewBinding.page.applyData(
                configuration = config,
                page = screening.successPage,
                pageType = EPageType.SUCCESS,
                action1 = { screeningFragment().next() },
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

        }
    }

}