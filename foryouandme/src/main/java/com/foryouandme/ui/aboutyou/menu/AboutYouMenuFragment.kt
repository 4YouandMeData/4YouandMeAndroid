package com.foryouandme.ui.aboutyou.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.ui.aboutyou.*
import com.foryouandme.ui.aboutyou.menu.compose.AboutYouMenuPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutYouMenuFragment : AboutYouSectionFragment() {

    private val viewModel: AboutYouMenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                AboutYouMenuPage(
                    aboutYouMenuViewModel = viewModel,
                    onBack = { back() },
                    onPregnancyClicked = {
                        navigator.navigateTo(
                            aboutYouNavController(),
                            AboutYouMenuPageToUserInfoPage
                        )
                    },
                    onDevicesClicked = {
                        navigator.navigateTo(
                            aboutYouNavController(),
                            AboutYouMenuToAppsAndDevices
                        )
                    },
                    onReviewConsentClicked = {
                        navigator.navigateTo(
                            aboutYouNavController(),
                            AboutYouMenuPageToAboutYouReviewConsentPage
                        )
                    },
                    onPermissionsClicked = {
                        navigator.navigateTo(
                            aboutYouNavController(),
                            AboutYouMenuPageToPermissionsPage
                        )
                    },
                    onDailySurveyTimeClicked = {
                        navigator.navigateTo(
                            aboutYouNavController(),
                            AboutYouMenuPageToDailySurveyTimePage
                        )
                    }
                )
            }
        }

}