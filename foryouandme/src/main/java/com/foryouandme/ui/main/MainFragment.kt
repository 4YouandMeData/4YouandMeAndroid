package com.foryouandme.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.activity.FYAMViewModel
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.action.ContextAction
import com.foryouandme.core.ext.execute
import com.foryouandme.core.ext.selectedUnselectedColor
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.notifiable.FeedAction
import com.foryouandme.ui.main.compose.MainPage
import com.foryouandme.ui.main.feeds.FeedsToTask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.welcome.view.*


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
                    },
                    openTask = { navigator.navigateTo(rootNavController(), MainToTask(it)) },
                    openUrl = { navigator.navigateTo(rootNavController(), AnywhereToWeb(it)) }
                )
            }
        }

    private fun feedNavigation(feedAction: FeedAction) {

        when (feedAction) {
            FeedAction.AboutYou ->
                navigator.navigateTo(rootNavController(), MainToAboutYou)
            FeedAction.Faq ->
                navigator.navigateTo(rootNavController(), MaiToFAQ)
            FeedAction.Rewards ->
                navigator.navigateTo(rootNavController(), MainToReward)
            FeedAction.Contacts ->
                navigator.navigateTo(rootNavController(), MainToInformation)
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