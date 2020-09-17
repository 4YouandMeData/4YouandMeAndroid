package org.fouryouandme.researchkit.step.video

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.camera.core.VideoCapture
import androidx.camera.view.CameraView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.step_video_diary.*
import kotlinx.android.synthetic.main.task.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.background.roundTopBackground
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.configuration.progressbar.progressDrawable
import org.fouryouandme.core.ext.*
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepFragment
import java.io.File


class VideoDiaryStepFragment : StepFragment(R.layout.step_video_diary) {

    private val videoDiaryViewModel: VideoDiaryViewModel by lazy {
        viewModelFactory(this, getFactory { VideoDiaryViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getVideoDirectory().deleteRecursively()

        videoDiaryViewModel.stateLiveData()
            .observeEvent {

                when (it) {
                    is VideoDiaryStateUpdate.RecordTime -> bindRecordingHeader()
                    is VideoDiaryStateUpdate.Recording -> {
                        bindRecordingState(it.recordingState)
                        bindRecordingHeader()
                    }
                }

            }

        videoDiaryViewModel.errorLiveData()
            .observeEvent {

                when (it.cause) {
                    VideoDiaryError.Recording ->
                        errorToast(it.error.message(requireContext()))
                }

            }

        videoDiaryViewModel.loadingLiveData()
            .observeEvent {
                when (it.task) {
                    VideoDiaryLoading.Merge -> loading.setVisibility(it.active)
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        video_view.isVisible = false

        camera.bindToLifecycle(this)
        camera.captureMode = CameraView.CaptureMode.VIDEO

    }

    override fun onResume() {
        super.onResume()

        startCoroutineAsync {

            if (videoDiaryViewModel.isInitialized().not()) {

                val step =
                    viewModel.getStepByIndexAs<Step.VideoDiaryStep>(indexArg()).orNull()

                step?.let { videoDiaryViewModel.initialize(it) }

            }

            evalOnMain { applyData() }

        }

    }

    private fun applyData(): Unit {

        val step = videoDiaryViewModel.state().step

        record_header.setTextColor(step.titleColor)

        record_info.background =
            roundTopBackground(step.infoBackgroundColor, 30)
        review_info.background =
            roundTopBackground(step.infoBackgroundColor, 30)

        close_recording.setImageResource(step.closeImage)
        close_recording.setOnClickListener { showCancelDialog() }
        close_review.setImageResource(step.closeImage)
        close_review.setOnClickListener { showCancelDialog() }

        recording_title.setTextColor(step.startRecordingDescriptionColor)

        recording_time.setTextColor(step.timeColor)
        review_time.setTextColor(step.reviewTimeColor)

        recording_time_image.setImageResource(step.timeImage)

        recording_progress.progressDrawable =
            progressDrawable(step.timeProgressBackgroundColor, step.timeProgressColor)
        recording_progress.max = 100
        recording_progress.progress = 50

        recording_info_title.text = step.infoTitle
        recording_info_title.setTextColor(step.infoTitleColor)

        recording_info_body.text = step.infoBody
        recording_info_body.setTextColor(step.infoBodyColor)

        review.background = button(step.buttonColor)
        review.setTextColor(step.buttonTextColor)
        review.setOnClickListener { startCoroutineAsync { review() } }
        review.text = step.reviewButton

        submit.background = button(step.buttonColor)
        submit.setTextColor(step.buttonTextColor)
        submit.setOnClickListener { }
        submit.text = step.submitButton

        bindRecordingState(videoDiaryViewModel.state().recordingState)
        bindRecordingHeader()

        taskFragment().toolbar.apply { hide() }

    }

    private fun bindRecordingState(state: RecordingState): Unit {

        val step = videoDiaryViewModel.state().step

        when (state) {
            RecordingState.Recording -> {

                recording_pause.setImageResource(step.pauseImage)

                recording_pause.setOnClickListener {

                    startCoroutineAsync { videoDiaryViewModel.pause() }
                    pause()

                }

                record_info.isVisible = false
                review_info.isVisible = false

            }
            is RecordingState.Pause -> {

                recording_pause.setImageResource(step.recordImage)

                recording_title.text = step.startRecordingDescription

                val currentRecordTime =
                    DateUtils.formatElapsedTime(videoDiaryViewModel.state().recordTimeSeconds)
                val maxRecordTime =
                    DateUtils.formatElapsedTime(videoDiaryViewModel.state().maxRecordTimeSeconds)

                val recordTimeLabel =
                    "$currentRecordTime/$maxRecordTime"

                recording_time.text = recordTimeLabel

                recording_progress.max = videoDiaryViewModel.state().maxRecordTimeSeconds.toInt()
                recording_progress.progress = videoDiaryViewModel.state().recordTimeSeconds.toInt()

                recording_pause.setOnClickListener {

                    val file = createVideoFile()
                    startCoroutineAsync { videoDiaryViewModel.record(file.absolutePath) }
                    record(file)

                }

                review.isEnabled = videoDiaryViewModel.state().recordTimeSeconds > 0

                record_info.isVisible = true
                review_info.isVisible = false

            }
            RecordingState.Review -> {

                val currentRecordTime =
                    DateUtils.formatElapsedTime(videoDiaryViewModel.state().recordTimeSeconds)
                val maxRecordTime =
                    DateUtils.formatElapsedTime(videoDiaryViewModel.state().maxRecordTimeSeconds)

                val recordTimeLabel =
                    "$currentRecordTime/$maxRecordTime"

                review_time.text = recordTimeLabel

                record_info.isVisible = false
                review_info.isVisible = true

            }
        }

    }

    private fun bindRecordingHeader(): Unit {

        when (videoDiaryViewModel.state().recordingState) {
            RecordingState.Recording -> {

                val currentRecordTime =
                    DateUtils.formatElapsedTime(videoDiaryViewModel.state().recordTimeSeconds)
                val maxRecordTime =
                    DateUtils.formatElapsedTime(videoDiaryViewModel.state().maxRecordTimeSeconds)

                val recordTimeLabel =
                    "$currentRecordTime/$maxRecordTime"

                record_header.text = recordTimeLabel

            }
            RecordingState.Pause ->
                record_header.text = videoDiaryViewModel.state().step.title
            RecordingState.Review ->
                record_header.text = videoDiaryViewModel.state().step.title
        }
    }

    private fun record(file: File): Unit {

        camera.startRecording(
            file,
            ContextCompat.getMainExecutor(requireContext()),
            object : VideoCapture.OnVideoSavedCallback {

                override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {

                    startCoroutineAsync { videoDiaryViewModel.pause() }

                }

                override fun onError(
                    videoCaptureError: Int,
                    message: String,
                    cause: Throwable?
                ) {

                    startCoroutineAsync { videoDiaryViewModel.handleRecordError() }

                }

            }
        )

    }

    private fun pause(): Unit {

        camera.stopRecording()

    }

    private suspend fun review(): Unit {

        val mergeDirectory = File(getVideoMergeDirectoryPath())

        if (mergeDirectory.exists().not())
            mergeDirectory.mkdir()

        videoDiaryViewModel.review(
            getVideoDirectoryPath(),
            mergeDirectory.absolutePath
        )
    }

    private fun createVideoFile(): File {

        val directory = getVideoDirectory()

        // crate also the video directory if not exist
        if (!directory.exists())
            directory.mkdir()

        return File(directory, "${System.currentTimeMillis()}.mp4")

    }

    private fun getVideoDirectory(): File = File(getVideoDirectoryPath())

    private fun getVideoDirectoryPath(): String =
        "${requireContext().applicationContext.filesDir.absolutePath}/video-diary"

    private fun getVideoMergeDirectoryPath(): String =
        "${getVideoDirectoryPath()}/merge"


    override fun onDestroy() {

        File(getVideoDirectoryPath()).deleteRecursively()

        super.onDestroy()
    }

}