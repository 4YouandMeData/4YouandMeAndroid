package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.auth.wearable.page.WearablePageFragmentDirections
import org.fouryouandme.auth.wearable.welcome.WearableWelcomeFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun wearableWelcomeToWearablePage(id: String): NavigationExecution = {
    it.navigate(WearableWelcomeFragmentDirections.actionWearableWelcomeToWearablePage(id))
}

fun wearablePageToWearablePage(id: String): NavigationExecution = {
    it.navigate(WearablePageFragmentDirections.actionWearablePageSelf(id))
}