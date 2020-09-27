package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.R
import org.fouryouandme.core.arch.navigation.NavigationExecution
import org.fouryouandme.tasks.TaskFragment

fun tasksToTask(type: String, id: String): NavigationExecution = {
    it.navigate(R.id.action_main_to_task, TaskFragment.getBundle(type, id))
}