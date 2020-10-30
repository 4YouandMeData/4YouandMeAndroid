package com.fouryouandme.main.feeds

import com.fouryouandme.core.arch.navigation.NavigationAction
import com.fouryouandme.core.entity.user.User
import com.giacomoparisi.recyclerdroid.core.DroidItem

data class FeedsState(
    val feeds: List<DroidItem<Any>> = emptyList(),
    val user: User?
)

sealed class FeedsStateUpdate {
    data class Initialization(
        val feeds: List<DroidItem<Any>>,
        val user: User?
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