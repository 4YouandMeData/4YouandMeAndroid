package com.foryouandme.ui.main.feeds

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.notifiable.FeedAction
import com.foryouandme.entity.user.User
import com.foryouandme.core.ext.*
import com.foryouandme.core.items.PagedRequestErrorItem
import com.foryouandme.core.items.PagedRequestErrorViewHolder
import com.foryouandme.core.items.PagedRequestLoadingItem
import com.foryouandme.core.items.PagedRequestLoadingViewHolder
import com.foryouandme.ui.main.MainSectionOldFragment
import com.foryouandme.ui.main.items.*
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import kotlinx.android.synthetic.main.feeds.*
import kotlinx.coroutines.launch
import java.text.MessageFormat


class FeedsFragment : MainSectionOldFragment<FeedsViewModel>(R.layout.feeds) {

    override val viewModel: FeedsViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                FeedsViewModel(
                    navigator,
                    injector.feedModule(),
                    injector.taskModule(),
                    injector.authModule(),
                    injector.analyticsModule()
                )
            }
        )
    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            TaskActivityViewHolder.factory {
                startCoroutineAsync { viewModel.executeTasks(rootNavController(), it) }
            },
            FeedRewardViewHolder.factory { item -> item.data.action?.let { feedNavigation(it) } },
            FeedEducationalViewHolder.factory { item -> item.data.action?.let { feedNavigation(it) } },
            FeedAlertViewHolder.factory { item -> item.data.action?.let { feedNavigation(it) } },
            DateViewHolder.factory(),
            QuickActivitiesViewHolder.factory(),
            FeedHeaderViewHolder.factory(),
            FeedEmptyViewHolder.factory(),
            PagedRequestLoadingViewHolder.factory(),
            PagedRequestErrorViewHolder.factory {

                configuration {
                    applyFeeds(viewModel.state().feeds)
                    viewModel.nextPage(rootNavController(), it)
                }

            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) { state ->
                when (state) {
                    is FeedsStateUpdate.Initialization -> {
                        applyFeeds(state.feeds)
                        configuration { applyUser(it, state.user) }
                    }
                    is FeedsStateUpdate.Feeds -> applyFeeds(state.feeds)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    FeedsLoading.Initialization ->
                        loading.setVisibility(it.active, viewModel.isInitialized())
                    FeedsLoading.QuickActivityUpload ->
                        loading.setVisibility(it.active, true)
                    is FeedsLoading.Feeds ->
                        if (it.task.page == 1)
                            loading.setVisibility(it.active, true)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) { errorPayload ->
                when (errorPayload.cause) {
                    FeedsError.Initialization ->
                        error.setError(errorPayload.error) {
                            configuration {
                                viewModel.initialize(rootNavController(), it)
                            }
                        }
                    FeedsError.QuickActivityUpload ->
                        errorToast(errorPayload.error)
                    is FeedsError.Feeds ->
                        if (errorPayload.cause.page == 1 || errorPayload.cause.page == -1)
                            error.setError(errorPayload.error) {
                                configuration { viewModel.reloadFeeds(rootNavController(), it) }
                            }
                        else configuration {
                            applyPageError(errorPayload.error, it)
                        }
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()

    }

    override fun onResume() {
        super.onResume()

        configuration {

            applyConfiguration(it)
            viewModel.initialize(rootNavController(), it)

        }
    }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

            header.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            title.setTextColor(configuration.theme.secondaryTextColor.color())

            header_text.setTextColor(configuration.theme.secondaryColor.color())
            header_text.alpha = 0.5f

            logo.setImageResource(imageConfiguration.logoStudySecondary())
            logo.setOnClickListener {
                startCoroutineAsync {
                    viewModel.aboutYouPage(rootNavController())
                }
            }

            swipe_refresh.setOnRefreshListener {
                startCoroutineAsync {
                    viewModel.initialize(rootNavController(), configuration)
                    swipe_refresh.isRefreshing = false
                }
            }

        }

    private suspend fun applyUser(configuration: Configuration, user: User?): Unit =
        evalOnMain {

            val pregnancyMonths = viewModel.getPregnancyMonths(user)
            val pregnancyTrimester = pregnancyMonths?.let { "${(it / 3) + 1}nd" }

            header_text.text =
                pregnancyTrimester?.let {
                    MessageFormat.format(configuration.text.tab.feedTitle, it)
                }
            header_text.isVisible = pregnancyTrimester != null && pregnancyMonths >= 0

            val pregnancyWeeks = viewModel.getPregnancyWeeks(user)

            title.text =
                pregnancyWeeks?.let {
                    MessageFormat.format(configuration.text.tab.feedSubTitle, it)
                }
            title.isVisible = pregnancyWeeks != null && pregnancyWeeks >= 0

        }

    private fun applyFeeds(feeds: PagedList<DroidItem<Any>>): Unit {

        val items =
            if (feeds.isCompleted) feeds.data
            else feeds.data.plus(PagedRequestLoadingItem)

        adapter.submitList(items)

    }

    private suspend fun applyPageError(
        error: ForYouAndMeError,
        configuration: Configuration
    ): Unit =
        evalOnMain {

            adapter.submitList(
                viewModel.state().feeds.data
                    .plus(listOf(PagedRequestErrorItem(configuration, error.message(requireContext()))))
            )
            swipe_refresh.isRefreshing = false

        }

    private fun setupList(): Unit {

        recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
        recycler_view.invalidateItemDecorations()
        recycler_view.addItemDecoration(
            LinearMarginItemDecoration(
                topMargin = {
                    when {
                        it.isOfType<FeedEmptyItem>() -> 50.dpToPx()
                        else -> 0.dpToPx()
                    }
                },
                startMargin = {
                    when {
                        it.isOfType<FeedHeaderItem>() -> 0
                        it.isOfType<QuickActivitiesItem>() -> 0
                        else -> 20.dpToPx()
                    }
                },
                endMargin = {
                    when {
                        it.isOfType<FeedHeaderItem>() -> 0
                        it.isOfType<QuickActivitiesItem>() -> 0
                        else -> 20.dpToPx()
                    }
                },
                bottomMargin = {
                    when {
                        it.isOfType<FeedEmptyItem>() -> 20.dpToPx()
                        else -> 0.dpToPx()
                    }
                }
            )
        )

        adapter.pageListener = {
            configuration { viewModel.nextPage(rootNavController(), it) }
        }

    }

    private fun feedNavigation(feedAction: FeedAction): Unit {
        lifecycleScope.launch {

            when (feedAction) {
                FeedAction.Feed -> {
                }
                FeedAction.Tasks -> mainViewModel.selectTasks()
                FeedAction.YourData -> mainViewModel.selectYourData()
                FeedAction.StudyInfo -> mainViewModel.selectStudyInfo()
                FeedAction.AboutYou -> viewModel.aboutYouPage(rootNavController())
                FeedAction.Faq -> viewModel.faq(rootNavController())
                FeedAction.Rewards -> viewModel.reward(rootNavController())
                FeedAction.Contacts -> viewModel.info(rootNavController())
                is FeedAction.Integration -> viewModel.openIntegrationApp(feedAction.app)
                is FeedAction.Web -> viewModel.web(rootNavController(), feedAction.url)
            }

        }

    }

}