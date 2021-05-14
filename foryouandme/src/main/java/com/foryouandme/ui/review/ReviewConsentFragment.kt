package com.foryouandme.ui.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.review.compose.ReviewConsentPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewConsentFragment : BaseFragment() {

    private val viewModel: ReviewConsentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                ReviewConsentPage(
                    reviewConsentViewModel = viewModel,
                    onBack = { navigator.back(rootNavController()) }
                )
            }
        }

}