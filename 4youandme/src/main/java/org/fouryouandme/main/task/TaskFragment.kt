package org.fouryouandme.main.task

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.task.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.setStatusBar

class TaskFragment : BaseFragment<TaskViewModel>(R.layout.task) {

    override val viewModel: TaskViewModel by lazy {
        viewModelFactory(this, getFactory { TaskViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is TaskStateUpdate.Initialization -> applyData(it.configuration)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent { loading.setVisibility(it.active, false) }

        viewModel.errorLiveData()
            .observeEvent { error.setError(it.error) { viewModel.initialize(rootNavController()) } }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state().configuration
            .fold(
                { viewModel.initialize(rootNavController()) },
                { applyData(it) }
            )
    }

    private fun applyData(configuration: Configuration): Unit {

        setStatusBar(configuration.theme.primaryColorStart.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        title.text = configuration.text.tab.taskTitle
        title.setTextColor(configuration.theme.secondaryColor.color())
        title.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()
    }
}