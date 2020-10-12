package org.fouryouandme.main.tasks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.decoration.LinearMarginItemDecoration
import kotlinx.android.synthetic.main.tasks.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*
import org.fouryouandme.main.MainSectionFragment
import org.fouryouandme.main.feeds.FeedHeaderItem
import org.fouryouandme.main.items.DateViewHolder
import org.fouryouandme.main.items.QuickActivitiesItem
import org.fouryouandme.main.items.QuickActivitiesViewHolder
import org.fouryouandme.main.items.TaskActivityViewHolder

class TasksFragment : MainSectionFragment<TasksViewModel>(R.layout.tasks) {

    override val viewModel: TasksViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                TasksViewModel(
                    navigator,
                    IORuntime,
                    injector.taskModule()
                )
            }
        )
    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            TaskActivityViewHolder.factory {
                startCoroutineAsync { viewModel.executeTasks(rootNavController(), it) }
            },
            DateViewHolder.factory(),
            QuickActivitiesViewHolder.factory()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) { state ->
                when (state) {
                    is TasksStateUpdate.Initialization ->
                        applyTasks(state.tasks)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    TasksLoading.Initialization ->
                        loading.setVisibility(it.active, viewModel.isInitialized())
                    TasksLoading.QuickActivityUpload ->
                        loading.setVisibility(it.active, true)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) {
                when (it.cause) {
                    TasksError.Initialization ->
                        error.setError(it.error) {
                            startCoroutineAsync {
                                viewModel.initialize(rootNavController(), configuration())
                            }
                        }
                    TasksError.QuickActivityUpload ->
                        errorToast(it.error)
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()

        // TODO: add the empty view as item
        empty.isVisible = false


    }

    override fun onResume() {
        super.onResume()

        configuration {

            applyData(it)
            viewModel.initialize(rootNavController(), it)
        }

    }

    private suspend fun applyData(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())

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
            empty_button.setOnClickListener { startCoroutineAsync { mainViewModel.selectFeed() } }

            swipe_refresh.setOnRefreshListener {
                startCoroutineAsync {
                    viewModel.initialize(rootNavController(), configuration)
                    swipe_refresh.isRefreshing = false
                }
            }

        }

    private fun applyTasks(tasks: List<DroidItem<Any>>): Unit {

        empty.isVisible = tasks.isEmpty()

        adapter.submitList(tasks)
    }

    private fun setupList(): Unit {

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

}