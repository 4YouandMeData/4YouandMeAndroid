package com.foryouandme.core.cases.feed

import arrow.core.Either
import com.foryouandme.core.arch.deps.modules.FeedModule
import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.data.api.feed.response.toFeedItems
import com.foryouandme.core.entity.feed.Feed
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


object FeedRepository {

    internal suspend fun FeedModule.fetchFeeds(
        token: String
    ): Either<ForYouAndMeError, List<Feed>> =

        errorModule.unwrapToEither { api.getFeeds(token) }
            .map { it.toFeedItems() }


    //TODO: portare fuori dal task il video
    internal suspend fun TaskModule.attachVideo(
        token: String,
        taskId: String,
        file: File
    ): Either<ForYouAndMeError, Unit> {

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