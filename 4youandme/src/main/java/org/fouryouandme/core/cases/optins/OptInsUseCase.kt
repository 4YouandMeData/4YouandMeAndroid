package org.fouryouandme.core.cases.optins

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.entity.optins.OptIns
import org.fouryouandme.core.ext.foldToKindEither
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object OptInsUseCase {

    fun <F> getOptIns(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, OptIns>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            !token.foldToKindEither(runtime.fx) {
                OptInsRepository.getOptIns(runtime, it)
            }

        }

    internal fun <F> setPermission(
        runtime: Runtime<F>,
        permissionId: String,
        agree: Boolean
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            val tz: TimeZone = TimeZone.getTimeZone("UTC")
            val df: DateFormat =
                SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm'Z'",
                    Locale.getDefault()
                )
            df.timeZone = tz
            val nowAsISO: String = df.format(Date())

            !token.foldToKindEither(runtime.fx) {
                OptInsRepository.setPermission(runtime, it, permissionId, agree, nowAsISO)
            }

        }

}