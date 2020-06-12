package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.NavigationDirections
import org.fouryouandme.R
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun anywhereToAuth(): NavigationExecution = {

    it.navigate(R.id.action_global_splash)
}

fun anywhereToWelcome(): NavigationExecution = {

    it.navigate(R.id.action_global_welcome)
}

fun anywhereToWeb(url: String): NavigationExecution = {

    it.navigate(NavigationDirections.actionGlobalWeb(url))

}

