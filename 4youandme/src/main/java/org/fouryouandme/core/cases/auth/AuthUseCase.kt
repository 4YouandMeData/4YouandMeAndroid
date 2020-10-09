package org.fouryouandme.core.cases.auth

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import org.fouryouandme.core.arch.deps.modules.AuthModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.cases.auth.AuthRepository.fetchUser
import org.fouryouandme.core.cases.auth.AuthRepository.loadToken
import org.fouryouandme.core.cases.auth.AuthRepository.login
import org.fouryouandme.core.cases.auth.AuthRepository.verifyPhoneNumber
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.entity.user.User

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

    internal suspend fun AuthModule.getUser(): Either<FourYouAndMeError, User?> =
        getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchUser(it) }

    internal suspend fun AuthModule.isLogged(): Boolean =
        getToken(CachePolicy.MemoryFirst).isRight()

}