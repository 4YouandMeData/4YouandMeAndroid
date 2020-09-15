package org.fouryouandme.researchkit.step.video

import arrow.core.getOrElse
import arrow.core.toOption
import arrow.fx.IO
import arrow.fx.extensions.fx
import com.googlecode.mp4parser.BasicContainer
import com.googlecode.mp4parser.authoring.Movie
import com.googlecode.mp4parser.authoring.Track
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator
import com.googlecode.mp4parser.authoring.tracks.AppendTrack
import kotlinx.coroutines.Dispatchers
import org.threeten.bp.Instant
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.*

fun mergeVideoDiary(videosPath: String, outputPath: String) =
    IO.fx {

        continueOn(Dispatchers.IO)


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

        val inMovies =
            videoFiles.map { MovieCreator.build(it.absolutePath) }

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

        val outputFileName = "merged_video_diary.mp4"
        val outputFilePath = "$outputPath/$outputFileName"

        val fc: FileChannel =
            RandomAccessFile(outputFilePath, "rw").channel

        out.writeContainer(fc)

        fc.close()


        outputFilePath

    }