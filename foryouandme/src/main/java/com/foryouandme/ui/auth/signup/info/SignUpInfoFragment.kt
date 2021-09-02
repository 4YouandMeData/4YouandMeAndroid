package com.foryouandme.ui.auth.signup.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.ui.auth.AuthSectionFragment
import com.foryouandme.ui.auth.signup.info.compose.SignUpInfoPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpInfoFragment : AuthSectionFragment() {

    private val viewModel: SignUpInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                SignUpInfoPage(
                    viewModel = viewModel,
                    onBack = { back() },
                    signIn = { navigator.navigateTo(authNavController(), it) },
                    signUpLater = {
                        navigator.navigateTo(authNavController(), SignUpInfoToSignUpLater)
                    }
                )
            }
        }

}