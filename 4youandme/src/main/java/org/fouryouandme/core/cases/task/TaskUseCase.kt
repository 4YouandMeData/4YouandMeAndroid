package org.fouryouandme.core.cases.task

import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.ext.foldToKindEither

object TaskUseCase {

    fun <F> getTasks(
        runtime: Runtime<F>
    ) =
        runtime.fx.concurrent {

            !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)
                .bind()
                .foldToKindEither(runtime.fx) { TaskRepository.getTasks(runtime, it) }

        }

}