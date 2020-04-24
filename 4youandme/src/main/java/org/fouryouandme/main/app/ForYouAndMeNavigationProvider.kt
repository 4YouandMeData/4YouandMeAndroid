package org.fouryouandme.main.app

import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.arch.navigation.NavigationExecution
import org.fouryouandme.core.arch.navigation.NavigationProvider

class ForYouAndMeNavigationProvider: NavigationProvider {

    override fun getNavigation(action: NavigationAction): NavigationExecution = {}

}