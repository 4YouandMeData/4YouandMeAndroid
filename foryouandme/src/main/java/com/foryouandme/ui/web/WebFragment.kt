package com.foryouandme.ui.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.web.compose.WebPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebFragment : BaseFragment() {

    private val args: WebFragmentArgs by navArgs()

    private val viewModel: WebViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                WebPage(
                    webViewModel = viewModel,
                    url = args.url,
                    type = args.type,
                    onBack = { navigator.back(rootNavController()) }
                )
            }
        }
}