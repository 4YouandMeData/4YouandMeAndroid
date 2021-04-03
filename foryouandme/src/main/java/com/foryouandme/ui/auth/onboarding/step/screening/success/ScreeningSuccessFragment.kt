package com.foryouandme.ui.auth.onboarding.step.screening.success

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.ext.removeBackButton
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.databinding.ScreeningPageBinding
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningSectionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreeningSuccessFragment : ScreeningSectionFragment(R.layout.screening_page) {

    private val binding: ScreeningPageBinding?
        get() = view?.let { ScreeningPageBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        applyData()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    override fun onScreeningUpdate() {
        super.onScreeningUpdate()
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
        val screening = screening

        if (viewBinding != null && config != null && screening != null) {

            setStatusBar(config.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(config.theme.secondaryColor.color())

            viewBinding.page.applyData(
                configuration = config,
                page = screening.successPage,
                pageType = EPageType.SUCCESS,
                action1 = { screeningFragment().next() },
                extraStringAction = { navigator.navigateTo(rootNavController(), AnywhereToWeb(it)) }
            )

        }
    }

}