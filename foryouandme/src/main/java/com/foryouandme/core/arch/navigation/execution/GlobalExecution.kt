package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.NavigationDirections
import com.foryouandme.R
import com.foryouandme.core.arch.navigation.NavigationExecution

fun anywhereToAuth(): NavigationExecution = {

    it.navigate(R.id.action_global_auth)
}

fun anywhereToWelcome(): NavigationExecution = {

    it.navigate(R.id.action_global_welcome)
}

fun anywhereToWeb(url: String): NavigationExecution = {

    it.navigate(NavigationDirections.actionGlobalWeb(url))

}

