package org.fouryouandme.core.cases.auth

import arrow.Kind
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.toOption
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.AuthModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.cases.auth.AuthRepository.loadToken
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

    @Deprecated("use suspend version")
    internal fun <F> getToken(
        runtime: Runtime<F>,
        cachePolicy: CachePolicy
    ): Kind<F, Either<FourYouAndMeError, String>> =
        runtime.fx.concurrent {

            when (cachePolicy) {
                CachePolicy.MemoryFirst ->
                    !Memory.token.toOption().toKind(runtime.fx) { loadToken(runtime) }
                CachePolicy.MemoryFirstRefresh ->
                    !Memory.token.toOption().toKind(runtime.fx) { loadToken(runtime) }
                CachePolicy.MemoryOrDisk ->
                    !Memory.token.toOption().toKind(runtime.fx) { loadToken(runtime) }
                CachePolicy.DiskFirst ->
                    !Memory.token.toOption().toKind(runtime.fx) { loadToken(runtime) }
                CachePolicy.DiskFirstRefresh ->
                    !Memory.token.toOption().toKind(runtime.fx) { loadToken(runtime) }
                CachePolicy.Network ->
                    !Memory.token.toOption().toKind(runtime.fx) { loadToken(runtime) }
            }.toEither { FourYouAndMeError.UserNotLoggedIn }

        }

    internal suspend fun AuthModule.getToken(
        cachePolicy: CachePolicy
    ): Either<FourYouAndMeError, String> =
        when (cachePolicy) {
            CachePolicy.MemoryFirst ->
                Memory.token ?: loadToken()
            CachePolicy.MemoryFirstRefresh ->
                Memory.token ?: loadToken()
            CachePolicy.MemoryOrDisk ->
                Memory.token ?: loadToken()
            CachePolicy.DiskFirst ->
                Memory.token ?: loadToken()
            CachePolicy.DiskFirstRefresh ->
                Memory.token ?: loadToken()
            CachePolicy.Network ->
                Memory.token ?: loadToken()
        }?.right() ?: FourYouAndMeError.UserNotLoggedIn.left()

}