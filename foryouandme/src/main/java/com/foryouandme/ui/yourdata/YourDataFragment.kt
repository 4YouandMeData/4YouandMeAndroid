package com.foryouandme.ui.yourdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.yourdata.compose.YourDataPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YourDataFragment : BaseFragment() {

    val viewModel: YourDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                YourDataPage(
                    yourDataViewModel = viewModel
                )
            }
        }

}