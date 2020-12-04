package com.foryouandme.ui.main.feeds

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.user.User
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.paging.PagedList

data class FeedsState(
    val feeds: PagedList<DroidItem<Any>> = PagedList.empty(),
    val user: User?
)

sealed class FeedsStateUpdate {

    data class Initialization(
        val feeds: PagedList<DroidItem<Any>>,
        val user: User?
    ) : FeedsStateUpdate()

    data class Feeds(
        val feeds: PagedList<DroidItem<Any>>
    ) : FeedsStateUpdate()
}

sealed class FeedsLoading {
    object Initialization : FeedsLoading()
    data class Feeds(val page: Int) : FeedsLoading()
    object QuickActivityUpload : FeedsLoading()
}

sealed class FeedsError {
    object Initialization : FeedsError()
    data class Feeds(val page: Int) : FeedsError()
    object QuickActivityUpload : FeedsError()
}

/* --- navigation --- */

data class FeedsToTask(val id: String) : NavigationAction