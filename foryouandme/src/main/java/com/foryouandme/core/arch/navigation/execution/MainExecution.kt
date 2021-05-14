package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.R
import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.htmldetails.EHtmlDetails
import com.foryouandme.ui.main.MainFragmentDirections
import com.foryouandme.ui.tasks.TaskFragment

fun mainToAboutYouPage(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToAboutYou())
    }

fun mainToInformation(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToHtmlDetails(EHtmlDetails.INFO))
    }

fun mainToReward(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToHtmlDetails(EHtmlDetails.REWARD))
    }

fun mainToFaq(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToHtmlDetails(EHtmlDetails.FAQ))
    }

fun mainToTask(id: String): NavigationExecution = {
    it.navigate(
        R.id.action_main_to_task,
        TaskFragment.getBundle(id, hashMapOf())
    )
}