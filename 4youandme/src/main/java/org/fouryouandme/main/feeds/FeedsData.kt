package org.fouryouandme.main.feeds

import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.navigation.NavigationAction

data class FeedsState(
    val feeds: List<DroidItem<Any>> = emptyList()
)

sealed class FeedsStateUpdate {
    data class Initialization(
        val feeds: List<DroidItem<Any>>
    ) : FeedsStateUpdate()
}

sealed class FeedsLoading {
    object Initialization : FeedsLoading()
    object QuickActivityUpload : FeedsLoading()
}

sealed class FeedsError {
    object Initialization : FeedsError()
    object QuickActivityUpload : FeedsError()
}

/* --- navigation --- */

data class FeedsToTask(val type: String, val id: String) : NavigationAction