package com.fouryouandme.core.arch.navigation.execution

import com.fouryouandme.core.arch.navigation.NavigationExecution
import com.fouryouandme.main.MainFragmentDirections

fun mainPageToAboutYouPage(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToAboutYou())
    }

fun mainPageToHtmlDetailsPage(pageId: Int): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToHtmlDetails(pageId))
    }