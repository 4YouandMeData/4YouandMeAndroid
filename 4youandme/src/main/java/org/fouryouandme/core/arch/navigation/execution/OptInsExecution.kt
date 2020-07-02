package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.auth.optin.welcome.OptInWelcomeFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun optInsWelcomeToOptInsPermission(id: String): NavigationExecution = {

    it.navigate(OptInWelcomeFragmentDirections.actionOptInWelcomeToOptInPermission(id))

}