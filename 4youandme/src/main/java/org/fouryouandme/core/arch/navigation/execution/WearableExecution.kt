package org.fouryouandme.core.arch.navigation.execution

import arrow.core.Option
import org.fouryouandme.auth.wearable.login.WearableLoginFragmentDirections
import org.fouryouandme.auth.wearable.page.WearablePageFragmentDirections
import org.fouryouandme.auth.wearable.welcome.WearableWelcomeFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun wearableWelcomeToWearablePage(id: String): NavigationExecution = {
    it.navigate(WearableWelcomeFragmentDirections.actionWearableWelcomeToWearablePage(id))
}

fun wearableWelcomeToWearableLogin(url: String, nextPageId: Option<String>): NavigationExecution = {
    it.navigate(
        WearableWelcomeFragmentDirections.actionWearableWelcomeToWearableLogin(
            url,
            nextPageId.orNull()
        )
    )
}

fun wearableWelcomeToWearableSuccess(): NavigationExecution = {
    it.navigate(WearableWelcomeFragmentDirections.actionWearableWelcomeToWearableSuccess())
}

fun wearablePageToWearablePage(id: String): NavigationExecution = {
    it.navigate(WearablePageFragmentDirections.actionWearablePageSelf(id))
}

fun wearablePageToWearableLogin(url: String, nextPageId: Option<String>): NavigationExecution = {
    it.navigate(
        WearablePageFragmentDirections.actionWearablePageToWearableLogin(
            url,
            nextPageId.orNull()
        )
    )
}

fun wearablePageToWearableSuccess(): NavigationExecution = {
    it.navigate(WearablePageFragmentDirections.actionWearablePageToWearableSuccess())
}

fun wearableLoginToWearablePage(id: String): NavigationExecution = {
    it.navigate(WearableLoginFragmentDirections.actionWearableLoginToWearablePage(id))
}

fun wearableLoginToWearableSuccess(): NavigationExecution = {
    it.navigate(WearableLoginFragmentDirections.actionWearableLoginToWearableSuccess())
}