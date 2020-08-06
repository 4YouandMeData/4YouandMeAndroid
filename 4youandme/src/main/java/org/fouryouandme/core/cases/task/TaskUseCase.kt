package org.fouryouandme.core.cases.task

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.entity.task.Task
import org.fouryouandme.core.ext.foldToKindEither

object TaskUseCase {

    fun <F> getTasks(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, List<Task>>> =
        runtime.fx.concurrent {

            !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)
                .bind()
                .foldToKindEither(runtime.fx) { TaskRepository.getTasks(runtime, it) }

        }

}