package com.fouryouandme.core.arch.navigation.execution

import com.fouryouandme.R
import com.fouryouandme.core.arch.navigation.NavigationExecution
import com.fouryouandme.tasks.TaskFragment

fun tasksToTask(type: String, id: String): NavigationExecution = {
    it.navigate(
        R.id.action_main_to_task,
        TaskFragment.getBundle(type, id, hashMapOf())
    )
}