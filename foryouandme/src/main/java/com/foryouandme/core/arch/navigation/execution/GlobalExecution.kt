package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.NavigationDirections
import com.foryouandme.R
import com.foryouandme.core.arch.navigation.NavigationExecution
import com.foryouandme.ui.auth.AuthFragment
import com.foryouandme.ui.web.EWebPageType

fun anywhereToAuth(): NavigationExecution = {

    it.navigate(R.id.action_global_auth, AuthFragment.build(false))
}

fun anywhereToConsent(): NavigationExecution = {

    it.navigate(R.id.action_global_auth, AuthFragment.build(true))
}

fun anywhereToWelcome(): NavigationExecution = {

    it.navigate(R.id.action_global_welcome)
}

fun anywhereToWeb(url: String, type: EWebPageType): NavigationExecution = {

    it.navigate(NavigationDirections.actionGlobalWeb(url, type))

}

