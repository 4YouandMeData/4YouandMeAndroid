package com.foryouandme.ui.auth.signin.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.ui.auth.AuthSectionFragment
import com.foryouandme.ui.auth.signin.pin.compose.PinCodePage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinCodeFragment : AuthSectionFragment() {

    private val viewModel: PinCodeViewModel by viewModels()

    @ExperimentalComposeUiApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                PinCodePage(
                    viewModel = viewModel,
                    onBack = { back() },
                    web = {
                        navigator.navigateTo(
                            rootNavController(),
                            AnywhereToWeb(it)
                        )
                    },
                    main = { navigator.navigateTo(rootNavController(), PinCodeToMain) },
                    onboarding = { navigator.navigateTo(authNavController(), PinCodeToOnboarding) }
                )
            }
        }
}