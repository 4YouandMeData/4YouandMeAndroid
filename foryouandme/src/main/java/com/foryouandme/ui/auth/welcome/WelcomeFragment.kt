package com.foryouandme.ui.auth.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.databinding.WelcomeBinding
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.AuthSectionFragment
import com.foryouandme.ui.auth.welcome.compose.WelcomePage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : AuthSectionFragment() {

    private val viewModel: WelcomeViewModel by viewModels()

    private val binding: WelcomeBinding?
        get() = view?.let { WelcomeBinding.bind(it) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                WelcomePage(
                    viewModel = viewModel,
                    onStartClicked = {
                        navigator.navigateTo(authNavController(), WelcomeToSignUpInfo)
                    }
                )
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*setupView()
        applyConfiguration()*/

    }

    override fun onResume() {
        super.onResume()

        viewModel.execute(WelcomeAction.ScreenViewed)

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {
            viewBinding.logo.setImageResource(imageConfiguration.logoStudy())
            viewBinding.welcomeImage.setImageResource(imageConfiguration.logoStudySecondary())
        }

    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val config = configuration

        if (viewBinding != null && config != null) {

            setStatusBar(config.theme.primaryColorStart.color())

            viewBinding.root.background =
                HEXGradient.from(
                    config.theme.primaryColorStart,
                    config.theme.primaryColorEnd
                ).drawable()

            viewBinding.start.background =
                button(config.theme.secondaryColor.color())
            viewBinding.start.text = config.text.welcome.startButton
            viewBinding.start.setTextColor(config.theme.primaryColorEnd.color())
            viewBinding.start.isAllCaps = false
            viewBinding.start.setOnClickListener {

            }

            viewBinding.start.alpha = 0f
            viewBinding.start.animate()
                .alpha(1f)
                .setDuration(800L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()

        }
    }

}