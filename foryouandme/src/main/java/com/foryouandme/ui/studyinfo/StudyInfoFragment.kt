package com.foryouandme.ui.studyinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.ui.main.MaiToFAQ
import com.foryouandme.ui.main.MainToAboutYou
import com.foryouandme.ui.main.MainToInformation
import com.foryouandme.ui.main.MainToReward
import com.foryouandme.ui.studyinfo.compose.StudyInfoPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyInfoFragment : BaseFragment() {

    private val viewModel: StudyInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                StudyInfoPage(
                    studyInfoViewModel = viewModel,
                    onAboutYouClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            MainToAboutYou
                        )
                    },
                    onContactClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            MainToInformation
                        )
                    },
                    onRewardsClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            MainToReward
                        )
                    },
                    onFAQClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            MaiToFAQ
                        )
                    }
                )
            }
        }
}