package org.fouryouandme.core.cases.integration

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.integration.Integration
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.noneToError
import org.fouryouandme.core.ext.unwrapToEither

object IntegrationRepository {

    internal fun <F> getIntegration(
        runtime: Runtime<F>,
        token: String,
        studyId: String
    ): Kind<F, Either<FourYouAndMeError, Integration>> =
        runtime.fx.concurrent {

            !runtime.injector.integrationApi
                .getIntegration(token, studyId)
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapResult(runtime.fx) { it.get().toIntegration(it) }
                .noneToError(runtime)

        }

}