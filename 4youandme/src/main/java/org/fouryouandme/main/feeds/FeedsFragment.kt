package org.fouryouandme.main.feeds

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.feeds.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.user.User
import org.fouryouandme.core.ext.*
import org.fouryouandme.main.MainSectionFragment
import org.fouryouandme.main.items.*
import java.text.MessageFormat


class FeedsFragment : MainSectionFragment<FeedsViewModel>(R.layout.feeds) {

    override val viewModel: FeedsViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                FeedsViewModel(
                    navigator,
                    injector.feedModule(),
                    injector.taskModule(),
                    injector.authModule()
                )
            }
        )
    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            TaskActivityViewHolder.factory {
                startCoroutineAsync { viewModel.executeTasks(rootNavController(), it) }
            },
            FeedRewardViewHolder.factory { },
            FeedEducationalViewHolder.factory { },
            FeedAlertViewHolder.factory { },
            DateViewHolder.factory(),
            QuickActivitiesViewHolder.factory(),
            FeedHeaderViewHolder.factory(),
            FeedEmptyViewHolder.factory()
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
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    FeedsLoading.Initialization ->
                        loading.setVisibility(it.active, viewModel.isInitialized())
                    FeedsLoading.QuickActivityUpload ->
                        loading.setVisibility(it.active, true)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) {
                when (it.cause) {
                    FeedsError.Initialization ->
                        error.setError(it.error) {
                            startCoroutineAsync {
                                viewModel.initialize(rootNavController(), configuration())
                            }
                        }
                    FeedsError.QuickActivityUpload ->
                        errorToast(it.error)
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

    private fun applyFeeds(feeds: List<DroidItem<Any>>): Unit {

        adapter.submitList(feeds)

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

    }
}