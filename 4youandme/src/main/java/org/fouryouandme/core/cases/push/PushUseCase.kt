package org.fouryouandme.core.cases.push

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.google.firebase.messaging.FirebaseMessaging
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.unknownError
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object PushUseCase {

    suspend fun getPushToken(): Either<FourYouAndMeError, String> =
        suspendCoroutine<Either<FourYouAndMeError, String>> { continuation ->

            FirebaseMessaging.getInstance().token
                .addOnCompleteListener {

                    val result = it.result

                    if (it.isSuccessful && result != null)
                        continuation.resume(result.right())
                    else
                        continuation.resume(unknownError().left())

                }

        }

}