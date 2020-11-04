package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.R
import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.main.MainFragmentDirections
import com.foryouandme.tasks.TaskFragment

fun mainPageToAboutYouPage(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToAboutYou())
    }

fun mainPageToHtmlDetailsPage(pageId: Int): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToHtmlDetails(pageId))
    }

fun mainToTask(id: String): NavigationExecution = {
    it.navigate(
        R.id.action_main_to_task,
        TaskFragment.getBundle(id, hashMapOf())
    )
}