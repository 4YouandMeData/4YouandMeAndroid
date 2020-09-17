package org.fouryouandme.researchkit.step.video

import arrow.fx.ForIO
import arrow.fx.coroutines.Disposable
import kotlinx.coroutines.delay
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.unknownError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.ext.startCoroutineCancellableAsync
import org.fouryouandme.researchkit.step.Step
import java.io.File

class VideoDiaryViewModel(navigator: Navigator, runtime: Runtime<ForIO>) :
    BaseViewModel<
            ForIO,
            VideoDiaryState,
            VideoDiaryStateUpdate,
            VideoDiaryError,
            Empty>(
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
                null
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
            VideoDiaryState.recordingState.modify(state()) { RecordingState.Pause }
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

        setErrorFx(unknownError(), VideoDiaryError.RecordingError)

        pause()

    }

}