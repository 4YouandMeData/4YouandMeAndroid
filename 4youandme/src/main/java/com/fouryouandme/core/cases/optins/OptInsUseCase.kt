package com.fouryouandme.core.cases.optins

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.OptInModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.optins.OptInsRepository.fetchOptIns
import com.fouryouandme.core.cases.optins.OptInsRepository.uploadPermission
import com.fouryouandme.core.entity.optins.OptIns
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

object OptInsUseCase {

    suspend fun OptInModule.getOptIns(): Either<FourYouAndMeError, OptIns?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchOptIns(it) }

    suspend fun OptInModule.setPermission(
        permissionId: String,
        agree: Boolean
    ): Either<FourYouAndMeError, Unit> =
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