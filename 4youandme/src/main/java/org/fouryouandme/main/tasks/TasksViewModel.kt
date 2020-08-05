package org.fouryouandme.main.tasks

import arrow.core.toOption
import arrow.fx.ForIO
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.task.TaskUseCase
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.main.items.DateItem
import org.threeten.bp.ZonedDateTime

class TasksViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        TasksState,
        TasksStateUpdate,
        TasksError,
        TasksLoading>
    (TasksState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(rootNavController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(TasksLoading.Initialization)

            val data =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)
                    .bind()
                    .foldToKindEither(runtime.fx) { config ->
                        TaskUseCase.getTasks(runtime).mapResult(runtime.fx) { config to it }
                    }
                    .handleAuthError(runtime, rootNavController, navigator)

            !data.fold(
                { setError(it, TasksError.Initialization) },
                {
                    setState(
                        state().copy(
                            configuration = it.first.toOption(),
                            tasks = taskMock(it.first)
                        ),
                        TasksStateUpdate.Initialization(it.first, taskMock(it.first))
                    )
                }
            )

            !hideLoading(TasksLoading.Initialization)

        }.unsafeRunAsync()


    // TODO: remove mock
    private fun taskMock(configuration: Configuration): List<DroidItem> {

        val list = mutableListOf<DroidItem>()

        list.add(
            DateItem(
                configuration,
                ZonedDateTime.now()
            )
        )

        return list
    }

}