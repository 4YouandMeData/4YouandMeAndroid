package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.auth.wearable.page.WearablePageFragmentDirections
import org.fouryouandme.auth.wearable.welcome.WearableWelcomeFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun wearableWelcomeToWearablePage(id: String): NavigationExecution = {
    it.navigate(WearableWelcomeFragmentDirections.actionWearableWelcomeToWearablePage(id))
}

fun wearableWelcomeToWearableLogin(url: String): NavigationExecution = {
    it.navigate(WearableWelcomeFragmentDirections.actionWearableWelcomeToWearableLogin(url))
}

fun wearablePageToWearablePage(id: String): NavigationExecution = {
    it.navigate(WearablePageFragmentDirections.actionWearablePageSelf(id))
}

fun wearablePageToWearableLogin(url: String): NavigationExecution = {
    it.navigate(WearablePageFragmentDirections.actionWearablePageToWearableLogin(url))
}