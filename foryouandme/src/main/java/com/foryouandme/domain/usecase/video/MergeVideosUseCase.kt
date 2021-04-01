package com.foryouandme.domain.usecase.video

import javax.inject.Inject

class MergeVideosUseCase @Inject constructor(
    private val repository: VideoRepository
) {

    suspend operator fun invoke(
        videosPath: String,
        outputPath: String,
        outputFileName: String
    ): String =
        repository.mergeVideos(videosPath, outputPath, outputFileName)

}