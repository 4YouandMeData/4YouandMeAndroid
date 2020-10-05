package org.fouryouandme.core.cases.auth

import arrow.Kind
import arrow.core.*
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.AuthModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.cases.auth.AuthRepository.loadToken
import org.fouryouandme.core.cases.auth.AuthRepository.login
import org.fouryouandme.core.cases.auth.AuthRepository.verifyPhoneNumber
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.entity.user.User
import org.fouryouandme.core.ext.toKind

object AuthUseCase {

    suspend fun AuthModule.verifyPhoneNumber(phone: String): Either<FourYouAndMeError, Unit> =
        configurationModule.getConfiguration(CachePolicy.MemoryFirst)
            .flatMap { verifyPhoneNumber(it, phone) }

    suspend fun AuthModule.login(
        phone: String,
        code: String
    ): Either<FourYouAndMeError, User> =
        configurationModule.getConfiguration(CachePolicy.MemoryFirst)
            .flatMap { login(it, phone, code) }

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