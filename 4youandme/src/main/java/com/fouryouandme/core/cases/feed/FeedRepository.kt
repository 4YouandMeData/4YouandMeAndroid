package com.fouryouandme.core.cases.feed

import arrow.core.Either
import com.fouryouandme.core.arch.deps.modules.FeedModule
import com.fouryouandme.core.arch.deps.modules.TaskModule
import com.fouryouandme.core.arch.deps.modules.unwrapToEither
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.data.api.feed.response.toFeedItems
import com.fouryouandme.core.entity.feed.Feed
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


object FeedRepository {

    internal suspend fun FeedModule.fetchFeeds(
        token: String
    ): Either<FourYouAndMeError, List<Feed>> =

        errorModule.unwrapToEither { api.getFeeds(token) }
            .map { it.toFeedItems() }


    //TODO: portare fuori dal task il video
    internal suspend fun TaskModule.attachVideo(
        token: String,
        taskId: String,
        file: File
    ): Either<FourYouAndMeError, Unit> {

        // create RequestBody instance from file
        val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "task[attachment]",
                "VideoDiary.mp4",
                requestFile
            )

        return errorModule.unwrapToEither { api.attach(token, taskId, body) }

    }
}