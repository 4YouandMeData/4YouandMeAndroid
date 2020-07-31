package org.fouryouandme.main.tasks

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import kotlinx.android.synthetic.main.tasks.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.setStatusBar
import org.fouryouandme.main.items.DateViewHolder
import org.fouryouandme.main.items.TaskViewHolder

class TasksFragment : BaseFragment<TasksViewModel>(R.layout.tasks) {

    override val viewModel: TasksViewModel by lazy {
        viewModelFactory(this, getFactory { TasksViewModel(navigator, IORuntime) })
    }

    private val adapter: DroidAdapter by lazy {
        DroidAdapter(TaskViewHolder.factory(), DateViewHolder.factory())
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

        viewModel.state().configuration
            .fold(
                { viewModel.initialize(rootNavController()) },
                { applyData(it, viewModel.state().tasks) }
            )
    }

    private fun applyData(configuration: Configuration, tasks: List<DroidItem>): Unit {

        setStatusBar(configuration.theme.primaryColorStart.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        title.text = configuration.text.tab.tasksTitle
        title.setTextColor(configuration.theme.secondaryColor.color())
        title.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

        applyTasks(tasks)
    }

    private fun applyTasks(tasks: List<DroidItem>): Unit = adapter.submitList(tasks)

    private fun setupList(): Unit {

        recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

    }
}