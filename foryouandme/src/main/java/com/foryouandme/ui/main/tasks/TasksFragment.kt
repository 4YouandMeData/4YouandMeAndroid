package com.foryouandme.ui.main.tasks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.ext.dpToPx
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.items.PagedRequestErrorItem
import com.foryouandme.core.items.PagedRequestErrorViewHolder
import com.foryouandme.core.items.PagedRequestLoadingItem
import com.foryouandme.core.items.PagedRequestLoadingViewHolder
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.main.MainSectionFragment
import com.foryouandme.ui.main.MainStateEvent
import com.foryouandme.ui.main.feeds.FeedHeaderItem
import com.foryouandme.ui.main.items.DateViewHolder
import com.foryouandme.ui.main.items.QuickActivitiesItem
import com.foryouandme.ui.main.items.QuickActivitiesViewHolder
import com.foryouandme.ui.main.items.TaskActivityViewHolder
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.tasks.*
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TasksFragment : MainSectionFragment(R.layout.tasks) {

    private val viewModel: TasksViewModel by viewModels()

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            TaskActivityViewHolder.factory { task ->
                task.data.activityType?.let {
                    navigator.navigateTo(
                        rootNavController(),
                        TasksToTask(task.data.taskId)
                    )
                }
            },
            DateViewHolder.factory(),
            QuickActivitiesViewHolder.factory(),
            PagedRequestLoadingViewHolder.factory(),
            PagedRequestErrorViewHolder.factory {

                applyTasks(viewModel.state.tasks)
                viewModel.execute(TasksStateEvent.GetTasksNextPage)

            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is TasksStateUpdate.Tasks ->
                        applyTasks(it.tasks)
                    is TasksStateUpdate.Config ->
                        applyData(it.configuration)

                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    is TasksLoading.Tasks ->
                        if (it.task.page == 1)
                            loading.setVisibility(it.active, true)
                    TasksLoading.QuickActivityUpload ->
                        loading.setVisibility(it.active, true)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach { uiError ->
                loading.setVisibility(false)
                when (uiError.cause) {
                    is TasksError.Tasks ->
                        if (uiError.cause.page == 1 || uiError.cause.page == -1)
                            error.setError(uiError.error, viewModel.state.configuration)
                            { viewModel.execute(TasksStateEvent.GetTasks) }
                        else
                            viewModel.state.configuration?.let { applyPageError(uiError.error, it) }
                    TasksError.QuickActivityUpload ->
                        errorToast(uiError.error, viewModel.state.configuration)
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()

        // TODO: add the empty view as item
        empty.isVisible = false

        val configuration = viewModel.state.configuration
        if (configuration != null) applyData(configuration)

    }

    override fun onResume() {
        super.onResume()

        viewModel.execute(TasksStateEvent.GetTasks)

    }

    private fun applyData(configuration: Configuration) {

        setStatusBar(configuration.theme.primaryColorStart.color())

        tasks_root.setBackgroundColor(configuration.theme.secondaryColor.color())

        title.text = configuration.text.tab.tasksTitle
        title.setTextColor(configuration.theme.secondaryColor.color())
        title.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

        empty_title.setTextColor(configuration.theme.primaryTextColor.color())
        empty_title.text = configuration.text.tab.tabTaskEmptyTitle

        empty_description.setTextColor(configuration.theme.primaryTextColor.color())
        empty_description.text = configuration.text.tab.tabTaskEmptySubtitle

        empty_button.setTextColor(configuration.theme.secondaryColor.color())
        empty_button.background = button(configuration.theme.primaryColorEnd.color())
        empty_button.text = configuration.text.tab.tabTaskEmptyButton
        empty_button.setOnClickListener {
            lifecycleScope.launchSafe { mainViewModel.execute(MainStateEvent.SelectFeed) }
        }

        swipe_refresh.setOnRefreshListener {
            viewModel.execute(TasksStateEvent.GetTasks)
            swipe_refresh.isRefreshing = false
        }

        adapter.pageListener = { viewModel.execute(TasksStateEvent.GetTasksNextPage) }

    }

    private fun applyTasks(tasks: PagedList<DroidItem<Any>>): Unit {

        empty.isVisible = tasks.isEmpty()

        val items =
            if (tasks.isCompleted) tasks.data
            else tasks.data.plus(PagedRequestLoadingItem)

        adapter.submitList(items)

    }

    private fun setupList() {

        recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
        recycler_view.invalidateItemDecorations()
        recycler_view.addItemDecoration(
            LinearMarginItemDecoration(
                { 0 },
                {
                    when {
                        it.isOfType<FeedHeaderItem>() -> 0
                        it.isOfType<QuickActivitiesItem>() -> 0
                        else -> 20.dpToPx()
                    }
                },
                {
                    when {
                        it.isOfType<FeedHeaderItem>() -> 0
                        it.isOfType<QuickActivitiesItem>() -> 0
                        else -> 20.dpToPx()
                    }
                },
                { 0 }
            )
        )

    }

    private fun applyPageError(error: Throwable, configuration: Configuration) {

        adapter.submitList(
            viewModel.state.tasks.data
                .plus(
                    listOf(
                        PagedRequestErrorItem(
                            configuration,
                            errorMessenger.getMessage(error, configuration)
                        )
                    )
                )
        )
        swipe_refresh.isRefreshing = false

    }


}