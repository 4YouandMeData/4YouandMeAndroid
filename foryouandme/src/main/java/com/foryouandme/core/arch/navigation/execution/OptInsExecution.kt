package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.auth.optin.OptInFragmentDirections
import com.foryouandme.auth.optin.permission.OptInPermissionFragmentDirections
import com.foryouandme.auth.optin.welcome.OptInWelcomeFragmentDirections
import com.foryouandme.core.arch.navigation.NavigationExecution

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