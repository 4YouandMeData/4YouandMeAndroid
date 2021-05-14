package com.foryouandme.ui.integration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.integration.compose.IntegrationLoginPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntegrationLoginFragment : BaseFragment() {

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
                    onBack = { navigator.back(rootNavController()) }
                )
            }
        }
}