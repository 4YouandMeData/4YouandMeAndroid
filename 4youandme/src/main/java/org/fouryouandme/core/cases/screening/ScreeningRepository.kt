package org.fouryouandme.core.cases.screening

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.screening.response.ScreeningResponse
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unwrapToEither

object ScreeningRepository {

    fun <F> getScreening(
        runtime: Runtime<F>,
        token: String,
        studyId: String
    ): Kind<F, Either<FourYouAndMeError, ScreeningResponse>> =
        runtime.fx.concurrent {

            !runtime.injector.screeningApi.getScreening(token, studyId)
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapResult(runtime.fx) { it.get() }

        }

}