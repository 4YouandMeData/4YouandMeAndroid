package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.main.MainFragmentDirections

fun mainPageToAboutYouPage(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToAboutYou())
    }

fun mainPageToHtmlDetailsPage(pageId: Int): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToHtmlDetails(pageId))
    }