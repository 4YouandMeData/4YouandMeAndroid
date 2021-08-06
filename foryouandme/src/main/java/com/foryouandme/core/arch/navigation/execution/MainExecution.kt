package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.R
import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.studyinfo.detail.EStudyInfoType
import com.foryouandme.ui.main.MainFragmentDirections
import com.foryouandme.ui.tasks.TaskFragment

fun mainToAboutYouPage(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToAboutYou())
    }

fun mainToStudyInfoDetail(type: EStudyInfoType): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToStudyInfoDetail(type))
    }

fun mainToTask(id: String): NavigationExecution = {
    it.navigate(
        R.id.action_main_to_task,
        TaskFragment.getBundle(id, hashMapOf())
    )
}