package com.foryouandme.core.cases.optins

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.OptInModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.optins.OptInsRepository.fetchOptIns
import com.foryouandme.core.cases.optins.OptInsRepository.uploadPermission
import com.foryouandme.core.entity.optins.OptIns
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

object OptInsUseCase {

    suspend fun OptInModule.getOptIns(): Either<ForYouAndMeError, OptIns?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchOptIns(it) }

    suspend fun OptInModule.setPermission(
        permissionId: String,
        agree: Boolean
    ): Either<ForYouAndMeError, Unit> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap {
                uploadPermission(
                    it,
                    permissionId,
                    agree,
                    ZonedDateTime.now(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"))
                )
            }

}