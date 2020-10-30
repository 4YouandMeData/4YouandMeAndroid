package com.fouryouandme.core.arch.navigation.execution

import com.fouryouandme.auth.optin.OptInFragmentDirections
import com.fouryouandme.auth.optin.permission.OptInPermissionFragmentDirections
import com.fouryouandme.auth.optin.welcome.OptInWelcomeFragmentDirections
import com.fouryouandme.core.arch.navigation.NavigationExecution

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