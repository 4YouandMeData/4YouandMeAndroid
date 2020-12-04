package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.R
import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.tasks.TaskFragment

fun feedsToTask(id: String): NavigationExecution = {
    it.navigate(
        R.id.action_main_to_task,
        TaskFragment.getBundle(id, hashMapOf())
    )
}