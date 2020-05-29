package org.fouryouandme.main.app.navigation

import org.fouryouandme.R
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun anywhereToAuth(): NavigationExecution = {

    it.navigate(R.id.action_global_splash)
}

fun anywhereToWelcome(): NavigationExecution = {

    it.navigate(R.id.action_global_welcome)
}

