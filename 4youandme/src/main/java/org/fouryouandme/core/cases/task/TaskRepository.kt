package org.fouryouandme.core.cases.task

import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.ext.unwrapToEither

object TaskRepository {

    internal fun <F> getTasks(
        runtime: Runtime<F>,
        token: String
    ) =
        runtime.fx.concurrent {

            !runtime.injector.taskApi.getTasks(token)
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)

        }
}