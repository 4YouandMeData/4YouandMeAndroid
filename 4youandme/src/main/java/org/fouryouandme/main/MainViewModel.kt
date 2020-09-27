package org.fouryouandme.main

import arrow.fx.ForIO
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator

class MainViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        MainState,
        MainStateUpdate,
        Empty,
        Empty>
    (MainState(), navigator, runtime) {

    /* --- page --- */

    suspend fun setRestorePage(id: Int): Unit {

        setStateFx(state().copy(restorePage = id)) { MainStateUpdate.RestorePage(id) }

    }

    suspend fun selectFeed(): Unit =
        setStateFx(
            state()
        ) { MainStateUpdate.PageNavigation(R.id.feed_navigation) }

    fun getPagedIds(): List<Int> =
        listOf(
            R.navigation.feed_navigation,
            R.navigation.tasks_navigation,
            R.navigation.user_data_navigation,
            R.navigation.study_info_navigation
        )

}