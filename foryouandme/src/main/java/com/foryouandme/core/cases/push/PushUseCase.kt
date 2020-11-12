package com.foryouandme.core.cases.push

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.unknownError
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object PushUseCase {

    suspend fun getPushToken(): Either<ForYouAndMeError, String> =
        suspendCoroutine { continuation ->

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