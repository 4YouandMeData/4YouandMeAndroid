package com.foryouandme.ui.main.feeds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.action.ContextAction
import com.foryouandme.core.ext.execute
import com.foryouandme.entity.notifiable.FeedAction
import com.foryouandme.ui.main.*
import com.foryouandme.ui.main.feeds.compose.FeedPage
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedsFragment : MainSectionFragment() {

    private val viewModel: FeedsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                FeedPage(
                    feedsViewModel = viewModel,
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
                    }
                )
            }
        }

    private fun feedNavigation(feedAction: FeedAction) {

        when (feedAction) {
            FeedAction.Feed -> {
            }
            FeedAction.Tasks ->
                mainViewModel.execute(MainStateEvent.SelectTasks)
            FeedAction.YourData ->
                mainViewModel.execute(MainStateEvent.SelectYourData)
            FeedAction.StudyInfo ->
                mainViewModel.execute(MainStateEvent.SelectStudyInfo)
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
        }

    }

}