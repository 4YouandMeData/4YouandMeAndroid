package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.auth.optin.OptInFragmentDirections
import org.fouryouandme.auth.optin.permission.OptInPermissionFragmentDirections
import org.fouryouandme.auth.optin.welcome.OptInWelcomeFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun optInWelcomeToOptInPermission(index: Int): NavigationExecution = {

    it.navigate(OptInWelcomeFragmentDirections.actionOptInWelcomeToOptInPermission(index))

}

fun optInPermissionToOptInPermission(index: Int): NavigationExecution = {

    it.navigate(OptInPermissionFragmentDirections.actionOptInPermissionSelf(index))

}

fun optInPermissionToOptInSuccess(): NavigationExecution = {

    it.navigate(OptInPermissionFragmentDirections.actionOptInPermissionToOptInSuccess())

}

fun optInToConsentUser(): NavigationExecution = {

    it.navigate(OptInFragmentDirections.actionOptInToConsentUser())

}