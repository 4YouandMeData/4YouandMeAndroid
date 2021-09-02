package com.foryouandme.ui.auth.signup.later

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.ui.auth.AuthSectionFragment
import com.foryouandme.ui.auth.signup.later.compose.SignUpLaterPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpLaterFragment : AuthSectionFragment() {

    private val viewModel: SignUpLaterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                SignUpLaterPage(
                    viewModel = viewModel,
                    onBack = { back() }
                )
            }
        }

}