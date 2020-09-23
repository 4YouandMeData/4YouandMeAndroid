package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.R
import org.fouryouandme.core.arch.navigation.NavigationExecution
import org.fouryouandme.researchkit.task.ETaskType
import org.fouryouandme.tasks.TaskFragment

fun tasksToTask(identifier: String, type: ETaskType): NavigationExecution = {
    it.navigate(R.id.action_main_to_task, TaskFragment.getBundle(identifier, type))
}