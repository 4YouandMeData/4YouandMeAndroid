package org.fouryouandme.core.cases.optins

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.OptInModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.optins.OptInsRepository.fetchOptIns
import org.fouryouandme.core.cases.optins.OptInsRepository.uploadPermission
import org.fouryouandme.core.entity.optins.OptIns
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