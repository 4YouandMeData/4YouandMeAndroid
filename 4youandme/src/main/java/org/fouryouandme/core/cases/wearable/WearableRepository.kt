package org.fouryouandme.core.cases.wearable

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.wearable.Wearable
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.noneToError
import org.fouryouandme.core.ext.unwrapToEither

object WearableRepository {

    internal fun <F> getWearable(
        runtime: Runtime<F>,
        token: String,
        studyId: String
    ): Kind<F, Either<FourYouAndMeError, Wearable>> =
        runtime.fx.concurrent {

            !runtime.injector.wearableApi
                .getWearable(token, studyId)
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapResult(runtime.fx) { it.get().toWearable(it) }
                .noneToError(runtime)

        }

}