package com.fouryouandme.core.ext

import arrow.core.Either
import arrow.fx.coroutines.evalOn
import com.fouryouandme.core.arch.deps.modules.ErrorModule
import com.fouryouandme.core.arch.deps.modules.unwrapToEither
import com.fouryouandme.core.arch.error.FourYouAndMeError
import kotlinx.coroutines.Dispatchers
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