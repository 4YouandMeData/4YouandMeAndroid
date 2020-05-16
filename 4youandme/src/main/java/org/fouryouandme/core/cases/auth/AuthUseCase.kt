package org.fouryouandme.core.cases.auth

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.auth.response.UserResponse

object AuthUseCase {

    fun <F> verifyPhoneNumber(
        runtime: Runtime<F>,
        phone: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        AuthRepository.verifyPhoneNumber(runtime, phone)

    fun <F> login(
        runtime: Runtime<F>,
        phone: String,
        code: String
    ): Kind<F, Either<FourYouAndMeError, UserResponse>> =
        AuthRepository.login(runtime, phone, code)
}