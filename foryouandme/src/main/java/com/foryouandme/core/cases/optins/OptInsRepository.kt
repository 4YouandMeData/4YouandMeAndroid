package com.foryouandme.core.cases.optins

import arrow.core.Either
import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.modules.OptInModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.data.repository.consent.user.network.request.OptInPermissionRequest
import com.foryouandme.data.repository.consent.user.network.request.OptInUserPermissionRequest
import com.foryouandme.entity.optins.OptIns

object OptInsRepository {

    internal suspend fun OptInModule.fetchOptIns(token: String): Either<ForYouAndMeError, OptIns?> =
        suspend { api.getOptIns(token, environment.studyId()) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toOptIns(it) }

    internal suspend fun OptInModule.uploadPermission(
        token: String,
        permissionId: String,
        agree: Boolean,
        batchCode: String
    ): Either<ForYouAndMeError, Unit> =
        OptInPermissionRequest(OptInUserPermissionRequest(agree, batchCode))
            .pipe { suspend { api.setPermission(token, permissionId, it) } }
            .pipe { errorModule.unwrapToEither(it) }

}