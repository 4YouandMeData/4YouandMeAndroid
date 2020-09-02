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
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*
import org.fouryouandme.main.MainViewModel
import org.fouryouandme.main.items.DateViewHolder
import org.fouryouandme.main.items.QuickActivitiesViewHolder
import org.fouryouandme.main.items.TaskActivityViewHolder

class TasksFragment : BaseFragment<TasksViewModel>(R.layout.tasks) {

    override val viewModel: TasksViewModel by lazy {
        viewModelFactory(this, getFactory { TasksViewModel(navigator, IORuntime) })
    }

    private val mainViewModel: MainViewModel by lazy {
        viewModelFactory(sectionParent(), getFactory { MainViewModel(navigator, IORuntime) })
    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(
            TaskActivityViewHolder.factory(),
            DateViewHolder.factory(),
            QuickActivitiesViewHolder.factory()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is TasksStateUpdate.Initialization -> applyData(it.configuration, it.tasks)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent { loading.setVisibility(it.active, false) }

        viewModel.errorLiveData()
            .observeEvent { error.setError(it.error) { viewModel.initialize(rootNavController()) } }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()

        empty.isVisible = false

        viewModel.state().configuration
            .fold(
                { viewModel.initialize(rootNavController()) },
                { applyData(it, viewModel.state().tasks) }
            )
    }

    private fun applyData(configuration: Configuration, tasks: List<DroidItem<Any>>): Unit {

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
        empty_button.setOnClickListener { mainViewModel.selectFeed() }

        applyTasks(tasks)
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
                { if (it.index == 0) 0 else 20.dpToPx() },
                { if (it.index == 0) 0 else 20.dpToPx() },
                { 0 }
            )
        )

    }
}