package org.fouryouandme.researchkit.step.video

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.fx.ForIO
import arrow.fx.coroutines.Disposable
import arrow.fx.coroutines.evalOn
import com.googlecode.mp4parser.BasicContainer
import com.googlecode.mp4parser.authoring.Movie
import com.googlecode.mp4parser.authoring.Track
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator
import com.googlecode.mp4parser.authoring.tracks.AppendTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.arch.error.unknownError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.permissionSettingsAction
import org.fouryouandme.core.cases.task.TaskUseCase.attachVideo
import org.fouryouandme.core.ext.startCoroutineCancellableAsync
import org.fouryouandme.researchkit.step.Step
import org.threeten.bp.Instant
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.*

class VideoDiaryViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val taskModule: TaskModule
) :
    BaseViewModel<
            ForIO,
            VideoDiaryState,
            VideoDiaryStateUpdate,
            VideoDiaryError,
            VideoDiaryLoading>(
        navigator = navigator,
        runtime = runtime
    ) {

    private var timer: Disposable? = null


    /* --- initialize --- */

    suspend fun initialize(step: Step.VideoDiaryStep): Unit =
        setStateSilentFx(
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
        setStateFx(
            state().copy(
                recordingState = RecordingState.Recording,
                startRecordTimeSeconds = state().recordTimeSeconds,
                lastRecordedFilePath = filePath
            )
        ) { VideoDiaryStateUpdate.Recording(state().recordingState) }

    }

    suspend fun pause() {

        timer?.invoke()
        setStateFx(
            VideoDiaryState.recordingState.modify(state()) { RecordingState.RecordingPause }
        ) { VideoDiaryStateUpdate.Recording(state().recordingState) }

    }

    private suspend fun resumeTimer(): Unit {

        delay(1000)
        incrementRecordTime()
        resumeTimer()

    }

    private suspend fun incrementRecordTime(): Unit =
        setStateFx(
            VideoDiaryState.recordTimeSeconds.modify(state()) { it + 1 }
        ) { VideoDiaryStateUpdate.RecordTime(state().recordTimeSeconds) }


    suspend fun handleRecordError(): Unit {

        // delete the last file if exist
        state().lastRecordedFilePath?.let {

            val file = File(it)

            if (file.exists()) file.delete()

        }

        // reset the time and the remove the deleted file path
        setStateSilentFx(
            state().copy(
                recordTimeSeconds = state().startRecordTimeSeconds,
                lastRecordedFilePath = null
            )
        )

        setErrorFx(unknownError(), VideoDiaryError.Recording)

        pause()

    }

    suspend fun toggleCamera(): Unit {

        setStateFx(
            VideoDiaryState.isBackCameraToggled.modify(state()) { it.not() }
        ) { VideoDiaryStateUpdate.Camera(it.isBackCameraToggled) }

        // disable the flash when the front camera is toggled
        if (state().isBackCameraToggled)
            setStateFx(
                VideoDiaryState.isFlashEnabled.modify(state()) { false }
            ) { VideoDiaryStateUpdate.Flash(it.isFlashEnabled) }

    }

    suspend fun toggleFlash(): Unit =
        setStateFx(
            VideoDiaryState.isFlashEnabled.modify(state()) { it.not() }
        ) { VideoDiaryStateUpdate.Flash(it.isFlashEnabled) }

    suspend fun merge(videosPath: String, outputPath: String, outputFileName: String): Unit {

        showLoadingFx(VideoDiaryLoading.Merge)

        // disable the flash when the user start the review flow
        if (state().isBackCameraToggled)
            setStateFx(
                VideoDiaryState.isFlashEnabled.modify(state()) { false }
            ) { VideoDiaryStateUpdate.Flash(it.isFlashEnabled) }

        val merge =
            Either.catch { mergeVideoDiary(videosPath, outputPath, outputFileName) }

        merge.fold(
            { setErrorFx(unknownError(), VideoDiaryError.Merge) },
            {
                setStateFx(
                    VideoDiaryState.recordingState.modify(state()) { RecordingState.Merged }
                ) { VideoDiaryStateUpdate.Recording(it.recordingState) }
            }
        )

        hideLoadingFx(VideoDiaryLoading.Merge)

    }

    suspend fun reviewPause(): Unit {

        setStateFx(
            VideoDiaryState.recordingState.modify(state()) { RecordingState.ReviewPause }
        ) { VideoDiaryStateUpdate.Recording(it.recordingState) }

    }

    suspend fun reviewPlay(): Unit {

        setStateFx(
            VideoDiaryState.recordingState.modify(state()) { RecordingState.Review }
        ) { VideoDiaryStateUpdate.Recording(it.recordingState) }

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

        showLoadingFx(VideoDiaryLoading.Upload)

        val upload = taskModule.attachVideo(taskId, file)

        upload.fold(
            {
                setErrorFx(it, VideoDiaryError.Upload)
            },
            {
                setStateFx(
                    VideoDiaryState.recordingState.modify(state()) { RecordingState.Uploaded })
                { VideoDiaryStateUpdate.Recording(it.recordingState) }
            }
        )

        hideLoadingFx(VideoDiaryLoading.Upload)

    }

    /* --- navigation --- */

    suspend fun permissionSettings(): Unit {

        navigator.performAction(permissionSettingsAction())

    }

}