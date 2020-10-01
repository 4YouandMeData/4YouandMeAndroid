package org.fouryouandme.core.ext

import arrow.core.Either
import arrow.fx.coroutines.evalOn
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.arch.deps.modules.ErrorModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import java.io.File


suspend fun File?.readJson(errorModule: ErrorModule): Either<FourYouAndMeError, String> =

    errorModule.unwrapToEither {

        evalOn(Dispatchers.IO) {

            val stringBuilder = StringBuilder()
            this!!.forEachLine {
                stringBuilder.append(it).append("\n")
            }

            // This response will have Json Format String
            stringBuilder.toString()
        }
    }