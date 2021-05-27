package com.foryouandme.ui.main.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.ui.main.MainSectionFragment
import com.foryouandme.ui.main.MainStateEvent
import com.foryouandme.ui.main.tasks.compose.TasksPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : MainSectionFragment() {

    private val viewModel: TasksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                TasksPage(
                    tasksViewModel = viewModel,
                    onFeedButtonClicked = { mainViewModel.execute(MainStateEvent.SelectFeed) },
                    onStartClicked = {
                        navigator.navigateTo(
                            rootNavController(),
                            TasksToTask(it.data.taskId)
                        )
                    }
                )
            }
        }

}