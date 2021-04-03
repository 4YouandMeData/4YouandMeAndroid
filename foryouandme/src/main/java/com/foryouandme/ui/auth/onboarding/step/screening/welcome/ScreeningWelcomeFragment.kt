package com.foryouandme.ui.auth.onboarding.step.screening.welcome

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.ext.hide
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.databinding.ScreeningPageBinding
import com.foryouandme.ui.auth.onboarding.step.screening.ScreeningSectionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreeningWelcomeFragment : ScreeningSectionFragment(R.layout.screening_welcome) {

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

        screeningFragment().binding?.toolbar?.hide()

    }

    private fun applyData() {

        val viewBinding = binding
        val config = configuration
        val screening = screening

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
                extraStringAction = { navigator.navigateTo(rootNavController(), AnywhereToWeb(it)) }
            )

            screeningFragment().hideAbort()

        }
    }

}