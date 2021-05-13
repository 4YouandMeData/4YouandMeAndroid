package com.foryouandme.ui.aboutyou.integration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import com.foryouandme.ui.aboutyou.integration.compose.IntegrationLoginPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntegrationLoginFragment : AboutYouSectionFragment() {

    private val args: IntegrationLoginFragmentArgs by navArgs()

    private val viewModel: IntegrationLoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                IntegrationLoginPage(
                    integrationLoginViewModel = viewModel,
                    url = args.url,
                    onBack = { back() }
                )
            }
        }
}