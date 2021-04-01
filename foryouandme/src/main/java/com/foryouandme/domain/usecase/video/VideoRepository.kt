package com.foryouandme.domain.usecase.video

interface VideoRepository {

    suspend fun mergeVideos(
        videosPath: String,
        outputPath: String,
        outputFileName: String
    ): String

}