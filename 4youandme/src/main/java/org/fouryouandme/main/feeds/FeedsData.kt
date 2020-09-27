package org.fouryouandme.main.feeds

import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.main.tasks.TasksError
import org.fouryouandme.main.tasks.TasksLoading
import org.fouryouandme.main.tasks.TasksStateUpdate

data class FeedsState(
    val tasks: List<DroidItem<Any>> = emptyList()
)

sealed class FeedsStateUpdate {
    data class Initialization(
        val tasks: List<DroidItem<Any>>
    ) : FeedsStateUpdate()
}

sealed class FeedsLoading {
    object Initialization : FeedsLoading()
}

sealed class FeedsError {
    object Initialization : FeedsError()
}

/* --- navigation --- */

data class FeedsToTask(val type: String, val id: String) : NavigationAction