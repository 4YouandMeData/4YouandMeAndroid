package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.core.arch.navigation.NavigationExecution
import org.fouryouandme.main.MainFragmentDirections

fun mainPageToAboutYouPage(): NavigationExecution =
    {
        it.navigate(MainFragmentDirections.actionMainToAboutYouFragment())
    }