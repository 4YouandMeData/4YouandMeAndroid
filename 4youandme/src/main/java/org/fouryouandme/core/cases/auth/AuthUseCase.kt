package org.fouryouandme.core.cases.auth

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError

object AuthUseCase {

    fun <F> verifyPhoneNumber(
        runtime: Runtime<F>,
        phone: String,
        missingPhoneErrorMessage: String
    ): Kind<F, Either<FourYouAndMeError, Unit>> =
        AuthRepository.verifyPhoneNumber(runtime, phone, missingPhoneErrorMessage)

}