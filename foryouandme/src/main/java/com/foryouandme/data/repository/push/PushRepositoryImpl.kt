package com.foryouandme.data.repository.push

import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.usecase.push.PushRepository
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PushRepositoryImpl @Inject constructor(

) : PushRepository {

    override suspend fun getPushToken(): String =
        suspendCoroutine { continuation ->

            var isResumed = false

            FirebaseMessaging.getInstance().token
                .addOnCompleteListener {

                    val result = it.result

                    if (isResumed.not() && it.isSuccessful && result != null) {
                        continuation.resume(result)
                        isResumed = true
                    } else {
                        isResumed = true
                        continuation.resumeWithException(ForYouAndMeException.Unknown)
                    }

                }

        }

}
