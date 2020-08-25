package org.fouryouandme.core.cases.task

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.task.response.toTaskItems
import org.fouryouandme.core.entity.task.Task
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unwrapToEither

object TaskRepository {

    internal fun <F> getTasks(
        runtime: Runtime<F>,
        token: String
    ): Kind<F, Either<FourYouAndMeError, List<Task>>> =
        runtime.fx.concurrent {

            !runtime.injector.taskApi.getTasks(token)
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapResult(runtime.fx) { it.toTaskItems() }

        }
}