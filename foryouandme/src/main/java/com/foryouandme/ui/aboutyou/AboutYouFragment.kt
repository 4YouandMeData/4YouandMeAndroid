package com.foryouandme.ui.aboutyou

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.aboutyou.compose.AboutYouPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutYouFragment : BaseFragment() {

    private val viewModel: AboutYouViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                AboutYouPage(
                    aboutYouViewModel = viewModel,
                    onBack = { navigator.back(rootNavController()) },
                    onPregnancyClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            AboutYouToUserInfo
                        )
                    },
                    onDevicesClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            AboutYouToAppsAndDevices
                        )
                    },
                    onReviewConsentClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            AboutYouToReviewConsent
                        )
                    },
                    onPermissionsClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            AboutYouToPermissions
                        )
                    },
                    onDailySurveyTimeClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            AboutYouToDailySurveyTime
                        )
                    }
                )
            }
        }

}