package org.fouryouandme.core.cases.optins

import arrow.core.Either
import arrow.syntax.function.pipe
import org.fouryouandme.core.arch.deps.modules.OptInModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.data.api.optins.request.OptInPermissionRequest
import org.fouryouandme.core.data.api.optins.request.OptInUserPermissionRequest
import org.fouryouandme.core.entity.optins.OptIns

object OptInsRepository {

    internal suspend fun OptInModule.fetchOptIns(token: String): Either<FourYouAndMeError, OptIns?> =
        suspend { api.getOptIns(token, environment.studyId()) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toOptIns(it) }

    internal suspend fun OptInModule.uploadPermission(
        token: String,
        permissionId: String,
        agree: Boolean,
        batchCode: String
    ): Either<FourYouAndMeError, Unit> =
        OptInPermissionRequest(OptInUserPermissionRequest(agree, batchCode))
            .pipe { suspend { api.setPermission(token, permissionId, it) } }
            .pipe { errorModule.unwrapToEither(it) }

}