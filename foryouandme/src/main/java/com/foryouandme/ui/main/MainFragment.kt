package com.foryouandme.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.activity.FYAMViewModel
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.navigation.AnywhereToConsent
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.action.ContextAction
import com.foryouandme.core.ext.execute
import com.foryouandme.entity.notifiable.FeedAction
import com.foryouandme.ui.main.compose.MainPage
import com.foryouandme.ui.main.feeds.FeedsToTask
import com.foryouandme.ui.studyinfo.detail.EStudyInfoType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : BaseFragment() {

    val viewModel: MainViewModel by viewModels()

    private val fyamViewModel: FYAMViewModel by viewModels({ requireActivity() })

    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                MainPage(
                    viewModel = viewModel,
                    onTaskActivityClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            FeedsToTask(it.data.taskId)
                        )
                    },
                    onFeedActionClicked = { feedNavigation(it) },
                    onLogoClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            MainToAboutYou
                        )
                    },
                    onAboutYouClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            MainToAboutYou
                        )
                    },
                    onContactClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            MainToStudyInfoDetail(EStudyInfoType.INFO)
                        )
                    },
                    onRewardsClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            MainToStudyInfoDetail(EStudyInfoType.REWARD)
                        )
                    },
                    onFAQClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            MainToStudyInfoDetail(EStudyInfoType.FAQ)
                        )
                    },
                    openTask = {
                        when(it) {
                            "new_consent_version_available" -> AnywhereToConsent
                            else -> navigator.navigateTo(rootNavController(), MainToTask(it))
                        }
                    },
                    openUrl = { navigator.navigateTo(rootNavController(), AnywhereToWeb(it)) }
                )
            }
        }

    private fun feedNavigation(feedAction: FeedAction) {

        when (feedAction) {
            FeedAction.AboutYou ->
                navigator.navigateTo(rootNavController(), MainToAboutYou)
            FeedAction.Faq ->
                navigator.navigateTo(
                    rootNavController(),
                    MainToStudyInfoDetail(EStudyInfoType.FAQ)
                )
            FeedAction.Rewards ->
                navigator.navigateTo(
                    rootNavController(),
                    MainToStudyInfoDetail(EStudyInfoType.REWARD)
                )
            FeedAction.Contacts ->
                navigator.navigateTo(
                    rootNavController(),
                    MainToStudyInfoDetail(EStudyInfoType.INFO)
                )
            FeedAction.Consent ->
                navigator.navigateTo(rootNavController(), AnywhereToConsent)
            is FeedAction.Integration ->
                requireContext().execute(ContextAction.OpenApp(feedAction.app.packageName))
            is FeedAction.Web ->
                navigator.navigateTo(rootNavController(), AnywhereToWeb(feedAction.url))
            else -> {

            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.execute(MainAction.HandleDeepLink(fyamViewModel.state))

    }

}