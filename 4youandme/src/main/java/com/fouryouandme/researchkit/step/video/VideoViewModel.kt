package com.fouryouandme.researchkit.step.video

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.fx.coroutines.Disposable
import arrow.fx.coroutines.evalOn
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.deps.modules.TaskModule
import com.fouryouandme.core.arch.error.unknownError
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.permissionSettingsAction
import com.fouryouandme.core.cases.task.TaskUseCase.attachVideo
import com.fouryouandme.core.ext.startCoroutineCancellableAsync
import com.googlecode.mp4parser.BasicContainer
import com.googlecode.mp4parser.authoring.Movie
import com.googlecode.mp4parser.authoring.Track
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator
import com.googlecode.mp4parser.authoring.tracks.AppendTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.threeten.bp.Instant
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.*

class VideoViewModel(
    navigator: Navigator,
    private val taskModule: TaskModule
) :
    BaseViewModel<
            VideoDiaryState,
            VideoStateUpdate,
            VideoError,
            VideoLoading>(
        navigator = navigator
    ) {

    private var timer: Disposable? = null


    /* --- initialize --- */

    suspend fun initialize(step: VideoStep): Unit =
        setStateSilent(
            VideoDiaryState(
                step,
                0,
                0,
                120,
                null,
                RecordingState.RecordingPause,
                false,
                true
            )
        )

    /* --- state --- */

    suspend fun record(filePath: String) {

        timer = startCoroutineCancellableAsync { resumeTimer() }
        setState(
            state().copy(
                recordingState = RecordingState.Recording,
                startRecordTimeSeconds = state().recordTimeSeconds,
                lastRecordedFilePath = filePath
            )
        ) { VideoStateUpdate.Recording(state().recordingState) }

    }

    suspend fun pause() {

        timer?.invoke()
        setState(
            VideoDiaryState.recordingState.modify(state()) { RecordingState.RecordingPause }
        ) { VideoStateUpdate.Recording(state().recordingState) }

    }

    private suspend fun resumeTimer(): Unit {

        delay(1000)
        incrementRecordTime()
        resumeTimer()

    }

    private suspend fun incrementRecordTime(): Unit =
        setState(
            VideoDiaryState.recordTimeSeconds.modify(state()) { it + 1 }
        ) { VideoStateUpdate.RecordTime(state().recordTimeSeconds) }


    suspend fun handleRecordError(): Unit {

        // delete the last file if exist
        state().lastRecordedFilePath?.let {

            val file = File(it)

            if (file.exists()) file.delete()

        }

        // reset the time and the remove the deleted file path
        setStateSilent(
            state().copy(
                recordTimeSeconds = state().startRecordTimeSeconds,
                lastRecordedFilePath = null
            )
        )

        setError(unknownError(), VideoError.Recording)

        pause()

    }

    suspend fun toggleCamera(): Unit {

        setState(
            VideoDiaryState.isBackCameraToggled.modify(state()) { it.not() }
        ) { VideoStateUpdate.Camera(it.isBackCameraToggled) }

        // disable the flash when the front camera is toggled
        if (state().isBackCameraToggled)
            setState(
                VideoDiaryState.isFlashEnabled.modify(state()) { false }
            ) { VideoStateUpdate.Flash(it.isFlashEnabled) }

    }

    suspend fun toggleFlash(): Unit =
        setState(
            VideoDiaryState.isFlashEnabled.modify(state()) { it.not() }
        ) { VideoStateUpdate.Flash(it.isFlashEnabled) }

    suspend fun merge(videosPath: String, outputPath: String, outputFileName: String): Unit {

        showLoading(VideoLoading.Merge)

        // disable the flash when the user start the review flow
        if (state().isBackCameraToggled)
            setState(
                VideoDiaryState.isFlashEnabled.modify(state()) { false }
            ) { VideoStateUpdate.Flash(it.isFlashEnabled) }

        val merge =
            Either.catch { mergeVideoDiary(videosPath, outputPath, outputFileName) }

        merge.fold(
            { setError(unknownError(), VideoError.Merge) },
            {
                setState(
                    VideoDiaryState.recordingState.modify(state()) { RecordingState.Merged }
                ) { VideoStateUpdate.Recording(it.recordingState) }
            }
        )

        hideLoading(VideoLoading.Merge)

    }

    suspend fun reviewPause(): Unit {

        setState(
            VideoDiaryState.recordingState.modify(state()) { RecordingState.ReviewPause }
        ) { VideoStateUpdate.Recording(it.recordingState) }

    }

    suspend fun reviewPlay(): Unit {

        setState(
            VideoDiaryState.recordingState.modify(state()) { RecordingState.Review }
        ) { VideoStateUpdate.Recording(it.recordingState) }

    }

    /* --- merge --- */


    private suspend fun mergeVideoDiary(
        videosPath: String,
        outputPath: String,
        outputFileName: String
    ): String =
        evalOn(Dispatchers.IO) { // ensure that run on IO dispatcher

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

            val outputFilePath = "$outputPath/$outputFileName"

            val fc: FileChannel =
                RandomAccessFile(outputFilePath, "rw").channel

            out.writeContainer(fc)

            fc.close()

            outputFilePath
        }

    /* --- submit --- */

    suspend fun submit(taskId: String, file: File): Unit {

        showLoading(VideoLoading.Upload)

        val upload = taskModule.attachVideo(taskId, file)

        upload.fold(
            {
                setError(it, VideoError.Upload)
            },
            {
                setState(
                    VideoDiaryState.recordingState.modify(state()) { RecordingState.Uploaded })
                { VideoStateUpdate.Recording(it.recordingState) }
            }
        )

        hideLoading(VideoLoading.Upload)

    }

    /* --- navigation --- */

    suspend fun permissionSettings(): Unit {

        navigator.performAction(permissionSettingsAction())

    }

}