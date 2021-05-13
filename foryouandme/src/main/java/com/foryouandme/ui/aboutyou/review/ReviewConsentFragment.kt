package com.foryouandme.ui.aboutyou.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import com.foryouandme.ui.aboutyou.review.compose.ReviewConsentPage
import com.foryouandme.ui.auth.onboarding.step.consent.review.info.ConsentReviewHeaderViewHolder
import com.foryouandme.ui.auth.onboarding.step.consent.review.info.ConsentReviewPageViewHolder
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewConsentFragment : AboutYouSectionFragment() {

    private val viewModel: ReviewConsentViewModel by viewModels()

    private val adapter: StableDroidAdapter by lazy {
        StableDroidAdapter(
            ConsentReviewHeaderViewHolder.factory(),
            ConsentReviewPageViewHolder.factory()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                ReviewConsentPage(
                    reviewConsentViewModel = viewModel,
                    onBack =  { back() }
                )
            }
        }

}