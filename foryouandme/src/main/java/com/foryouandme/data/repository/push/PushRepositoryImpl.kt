package com.foryouandme.data.repository.push

import com.foryouandme.core.ext.catch
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

            catch(
                {
                    FirebaseMessaging.getInstance().token
                        .addOnCompleteListener {
                            catch(
                                {
                                    val result = it.result

                                    if (isResumed.not()) {
                                        if (it.isSuccessful && result != null) {
                                            continuation.resume(result)
                                            isResumed = true
                                        } else {
                                            isResumed = true
                                            continuation.resumeWithException(
                                                ForYouAndMeException.Unknown
                                            )
                                        }
                                    }
                                },
                                {
                                    if (isResumed.not()) {
                                        isResumed = true
                                        continuation.resumeWithException(it)
                                    }
                                }
                            )
                        }
                },
                {
                    if (isResumed.not()) {
                        isResumed = true
                        continuation.resumeWithException(it)
                    }
                }
            )
        }

}
