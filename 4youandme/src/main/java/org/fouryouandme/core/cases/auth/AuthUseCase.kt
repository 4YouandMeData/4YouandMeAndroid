package org.fouryouandme.core.cases.auth

import arrow.Kind
import arrow.core.Either
import arrow.core.Option
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.entity.user.User
import org.fouryouandme.core.ext.toKind

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
    ): Kind<F, Either<FourYouAndMeError, User>> =
        AuthRepository.login(runtime, phone, code)

    internal fun <F> getToken(
        runtime: Runtime<F>,
        cachePolicy: CachePolicy
    ): Kind<F, Option<String>> =
        runtime.fx.concurrent {

            when (cachePolicy) {
                CachePolicy.MemoryFirst ->
                    !Memory.token.toKind(runtime.fx) { AuthRepository.loadToken(runtime) }
                CachePolicy.MemoryFirstRefresh ->
                    !Memory.token.toKind(runtime.fx) { AuthRepository.loadToken(runtime) }
                CachePolicy.MemoryOrDisk ->
                    !Memory.token.toKind(runtime.fx) { AuthRepository.loadToken(runtime) }
                CachePolicy.DiskFirst ->
                    !Memory.token.toKind(runtime.fx) { AuthRepository.loadToken(runtime) }
                CachePolicy.DiskFirstRefresh ->
                    !Memory.token.toKind(runtime.fx) { AuthRepository.loadToken(runtime) }
                CachePolicy.Network ->
                    !Memory.token.toKind(runtime.fx) { AuthRepository.loadToken(runtime) }
            }

        }
}