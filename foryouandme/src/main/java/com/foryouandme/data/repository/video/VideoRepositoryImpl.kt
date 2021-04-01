package com.foryouandme.data.repository.video

import arrow.core.getOrElse
import arrow.core.toOption
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.domain.usecase.video.VideoRepository
import com.googlecode.mp4parser.BasicContainer
import com.googlecode.mp4parser.authoring.Movie
import com.googlecode.mp4parser.authoring.Track
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator
import com.googlecode.mp4parser.authoring.tracks.AppendTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class VideoRepositoryImpl @Inject constructor() : VideoRepository {

    override suspend fun mergeVideos(
        videosPath: String,
        outputPath: String,
        outputFileName: String
    ): String =
        withContext(Dispatchers.IO) { // ensure that run on IO dispatcher

            val directory = File(videosPath)

            val videoFiles =
                directory.listFiles()
                    .toOption()
                    .map { it.toList() }
                    .getOrElse { emptyList() }
                    .filter { it.path.endsWith("mp4") }
                    .sortedWith { o1, o2 ->

                        val o1Instant = Instant.ofEpochMilli(o1.lastModified())
                        val o2Instant = Instant.ofEpochMilli(o2.lastModified())

                        o1Instant.compareTo(o2Instant)
                    }

            val inMovies = videoFiles.map { MovieCreator.build(it.absolutePath) }

            val videoTracks: MutableList<Track> = LinkedList()
            val audioTracks: MutableList<Track> = LinkedList()

            inMovies.forEach { movie ->
                movie.tracks.forEach {
                    if (it.handler == "soun") audioTracks.add(it)
                    if (it.handler == "vide") videoTracks.add(it)
                }
            }

            val result = Movie()

            if (audioTracks.size > 0)
                result.addTrack(AppendTrack(*audioTracks.toTypedArray()))
            if (videoTracks.size > 0)
                result.addTrack(AppendTrack(*videoTracks.toTypedArray()))

            val out = DefaultMp4Builder().build(result) as BasicContainer

            val outputFilePath = "$outputPath/$outputFileName"

            val fc: FileChannel =
                RandomAccessFile(outputFilePath, "rw").channel

            out.writeContainer(fc)

            fc.close()

            compressVideo(outputFilePath, outputPath)

            outputFilePath
        }

    private suspend fun compressVideo(sourceFilePath: String, outputPath: String) {

        val qualities = listOf(
            VideoQuality.VERY_HIGH,
            VideoQuality.HIGH,
            VideoQuality.MEDIUM,
            VideoQuality.LOW,
            VideoQuality.VERY_LOW
        )
        var qualityIndex = 0

        // if the file is still big compress again
        while (getFileSizeMb(sourceFilePath) > 18 && qualityIndex < qualities.size) {

            val quality = qualities[qualityIndex]
            compressVideo(
                sourceFilePath,
                outputPath,
                quality
            )
            qualityIndex += 1

        }

    }

    private suspend fun compressVideo(
        sourceFilePath: String,
        outputPath: String,
        quality: VideoQuality
    ) {
        suspendCoroutine<Unit> {

            val outputFilePath = "$outputPath/compressed.mp4"

            VideoCompressor.start(
                sourceFilePath,
                outputFilePath,
                object : CompressionListener {

                    override fun onProgress(percent: Float) {
                        // Update UI with progress value
                    }

                    override fun onStart() {
                        // Compression start
                    }

                    override fun onSuccess() {
                        // On Compression success
                        // delete source file
                        File(sourceFilePath).deleteOnExit()
                        // rename compressed file with the same name of the source
                        File(outputFilePath).renameTo(File(sourceFilePath))
                        it.resume(Unit)
                    }

                    override fun onFailure(failureMessage: String) {
                        // On Failure
                        it.resumeWithException(ForYouAndMeException.Unknown)
                    }

                    override fun onCancelled() {
                        // On Cancelled
                        it.resumeWithException(ForYouAndMeException.Unknown)
                    }

                },
                quality,
                isMinBitRateEnabled = false,
                keepOriginalResolution = false
            )
        }

    }

    private fun getFileSizeMb(filePath: String): Float {

        val bytes: Long = File(filePath).length()
        val kilobytes = bytes / 1024f
        return kilobytes / 1024f

    }

}