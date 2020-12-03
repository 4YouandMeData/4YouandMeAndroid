package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.R
import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.htmldetails.EHtmlDetails
import com.foryouandme.main.MainFragmentDirections
import com.foryouandme.tasks.TaskFragment

fun mainPageToAboutYouPage(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToAboutYou())
    }

fun mainPageToInformation(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToHtmlDetails(EHtmlDetails.INFO))
    }

fun mainPageToReward(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToHtmlDetails(EHtmlDetails.REWARD))
    }

fun mainPageToFaq(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToHtmlDetails(EHtmlDetails.FAQ))
    }

fun mainToTask(id: String): NavigationExecution = {
    it.navigate(
        R.id.action_main_to_task,
        TaskFragment.getBundle(id, hashMapOf())
    )
}