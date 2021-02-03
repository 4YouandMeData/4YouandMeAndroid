package com.foryouandme.core.ext

import arrow.core.Either
import arrow.fx.coroutines.evalOn
import com.foryouandme.core.arch.deps.modules.ErrorModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import kotlinx.coroutines.Dispatchers
import java.io.File


suspend fun File?.readJson(errorModule: ErrorModule): Either<ForYouAndMeError, String> =

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

fun File?.readJson(): String? {

    val stringBuilder = StringBuilder()

    return if (this != null) {

        forEachLine {
            stringBuilder.append(it).append("\n")
        }

        // This response will have Json Format String
        stringBuilder.toString()

    } else null

}