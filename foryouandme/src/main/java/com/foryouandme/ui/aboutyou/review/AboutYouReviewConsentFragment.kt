package com.foryouandme.ui.aboutyou.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.ui.aboutyou.AboutYouSectionFragment
import com.foryouandme.ui.aboutyou.review.compose.AboutYouReviewConsentPage
import com.foryouandme.ui.auth.onboarding.step.consent.review.info.ConsentReviewHeaderViewHolder
import com.foryouandme.ui.auth.onboarding.step.consent.review.info.ConsentReviewPageViewHolder
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutYouReviewConsentFragment : AboutYouSectionFragment() {

    private val viewModel: AboutYouReviewConsentViewModel by viewModels()

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
                AboutYouReviewConsentPage(
                    aboutYouReviewConsentViewModel = viewModel,
                    onBack =  { back() }
                )
            }
        }

}