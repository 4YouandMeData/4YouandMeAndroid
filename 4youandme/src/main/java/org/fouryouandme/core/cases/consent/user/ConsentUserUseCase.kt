package org.fouryouandme.core.cases.consent.user

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.entity.consent.user.ConsentUser
import org.fouryouandme.core.ext.foldToKindEither

object ConsentUserUseCase {

    fun <F> getConsent(runtime: Runtime<F>): Kind<F, Either<FourYouAndMeError, ConsentUser>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            !token.foldToKindEither(runtime.fx) {

                ConsentUserRepository.getConsent(
                    runtime,
                    it,
                    runtime.injector.environment.studyId()
                )

            }

        }

    fun <F> createUserConsent(
        runtime: Runtime<F>,
        email: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            !token.foldToKindEither(runtime.fx) {

                ConsentUserRepository.createUserConsent(
                    runtime,
                    it,
                    runtime.injector.environment.studyId(),
                    email
                )

            }

        }

    internal fun <F> updateUserConsent(
        runtime: Runtime<F>,
        firstName: String,
        lastName: String,
        signatureBase64: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            !token.foldToKindEither(runtime.fx) {

                ConsentUserRepository.updateUserConsent(
                    runtime,
                    it,
                    runtime.injector.environment.studyId(),
                    firstName,
                    lastName,
                    signatureBase64
                )

            }

        }

    internal fun <F> confirmEmail(
        runtime: Runtime<F>,
        code: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            !token.foldToKindEither(runtime.fx) {

                ConsentUserRepository.confirmEmail(
                    runtime,
                    it,
                    runtime.injector.environment.studyId(),
                    code
                )

            }

        }

    internal fun <F> resendConfirmationEmail(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            !token.foldToKindEither(runtime.fx) {

                ConsentUserRepository.resendConfirmationEmail(
                    runtime,
                    it,
                    runtime.injector.environment.studyId()
                )

            }

        }
}