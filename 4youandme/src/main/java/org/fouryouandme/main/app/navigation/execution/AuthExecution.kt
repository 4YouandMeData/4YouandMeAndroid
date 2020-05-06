package org.fouryouandme.main.app.navigation.execution

import org.fouryouandme.R
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun splashToWelcome(): NavigationExecution = {
    it.navigate(R.id.action_splash_to_welcome)
}