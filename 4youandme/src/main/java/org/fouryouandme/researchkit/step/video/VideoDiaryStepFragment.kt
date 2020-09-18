package org.fouryouandme.researchkit.step.video

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.MediaController
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.VideoCapture
import androidx.camera.view.CameraView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import arrow.core.Either
import kotlinx.android.synthetic.main.step_video_diary.*
import kotlinx.android.synthetic.main.task.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.background.roundTopBackground
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.configuration.progressbar.progressDrawable
import org.fouryouandme.core.ext.*
import org.fouryouandme.core.permission.Permission
import org.fouryouandme.core.permission.PermissionError
import org.fouryouandme.core.permission.requestMultiplePermission
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepFragment
import java.io.File


class VideoDiaryStepFragment : StepFragment(R.layout.step_video_diary) {

    private val mediaController: MediaController by lazy { MediaController(requireContext()) }

    private val videoDiaryViewModel: VideoDiaryViewModel by lazy {
        viewModelFactory(this, getFactory {
            VideoDiaryViewModel(
                navigator,
                IORuntime,
                injector.taskModule()
            )
        })
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
                    is VideoDiaryStateUpdate.Flash ->
                        bindFlash(it.isFlashEnabled)
                    is VideoDiaryStateUpdate.Camera ->
                        bindCamera(it.isBackCameraToggled)
                }

            }

        videoDiaryViewModel.errorLiveData()
            .observeEvent {

                when (it.cause) {
                    VideoDiaryError.Recording ->
                        errorToast(it.error.message(requireContext()))
                    VideoDiaryError.Merge ->
                        errorToast(it.error.message(requireContext()))
                    VideoDiaryError.Upload ->
                        errorToast(it.error.message(requireContext()))
                }

            }

        videoDiaryViewModel.loadingLiveData()
            .observeEvent {
                when (it.task) {
                    VideoDiaryLoading.Merge -> loading.setVisibility(it.active)
                    VideoDiaryLoading.Upload -> loading.setVisibility(it.active)
                }
            }

        startCoroutineAsync {

            if (videoDiaryViewModel.isInitialized().not()) {

                val step =
                    viewModel.getStepByIndexAs<Step.VideoDiaryStep>(indexArg())

                step?.let { videoDiaryViewModel.initialize(it) }

            }

            setupCamera(videoDiaryViewModel.state().step)
        }

    }

    @SuppressLint("MissingPermission")
    private suspend fun setupCamera(step: Step.VideoDiaryStep): Unit {

        val permsission =
            requestMultiplePermission(requireContext(), Permission.Camera, Permission.RecordAudio)

        permsission.fold(
            {
                when (it) {
                    is PermissionError.PermissionDenied ->
                        handleMissingPermission(it.permission, step)
                    PermissionError.Unknown ->
                        viewModel.close(taskNavController())
                }
            },
            {
                /* All permission are granted */
                evalOnMain {

                    camera.isVisible = true
                    camera.bindToLifecycle(this)
                    camera.captureMode = CameraView.CaptureMode.VIDEO

                }

            }
        )
    }

    private suspend fun handleMissingPermission(
        permission: Permission,
        step: Step.VideoDiaryStep
    ): Unit {

        when (permission) {
            Permission.Camera ->
                showPermissionError(
                    step.missingPermissionCamera,
                    step.missingPermissionCameraBody,
                    step.settings,
                    step.cancel
                )
            Permission.RecordAudio ->
                showPermissionError(
                    step.missingPermissionMic,
                    step.missingPermissionMicBody,
                    step.settings,
                    step.cancel
                )
        }

    }

    private suspend fun showPermissionError(
        title: String,
        description: String,
        settings: String,
        cancel: String
    ): Unit {

        evalOnMain {
            AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(settings) { _, _ ->
                    startCoroutineAsync {
                        videoDiaryViewModel.permissionSettings()
                        viewModel.close(taskNavController())
                    }
                }
                .setNegativeButton(cancel) { _, _ ->
                    startCoroutineAsync { viewModel.close(taskNavController()) }
                }
                .setCancelable(false)
                .show()
        }

    }

    override fun onResume() {
        super.onResume()

        startCoroutineAsync {

            if (videoDiaryViewModel.isInitialized().not()) {

                val step =
                    viewModel.getStepByIndexAs<Step.VideoDiaryStep>(indexArg())

                step?.let { videoDiaryViewModel.initialize(it) }

            }

            evalOnMain { setupUI() }

        }

    }

    private fun setupUI(): Unit {

        val step = videoDiaryViewModel.state().step

        title.setTextColor(step.titleColor)

        camera_toggle.setImageResource(imageConfiguration.videoDiaryToggleCamera())
        camera_toggle.setOnClickListener {
            startCoroutineAsync { videoDiaryViewModel.toggleCamera() }
        }

        flash_toggle.setOnClickListener {
            startCoroutineAsync { videoDiaryViewModel.toggleFlash() }
        }

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
        submit.setOnClickListener {
            startCoroutineAsync {
                videoDiaryViewModel.submit(viewModel.state().task.identifier, getVideoMergeFile())
            }
        }
        submit.text = step.submitButton

        bindRecordingState(videoDiaryViewModel.state().recordingState)
        bindRecordingHeader()
        bindFlash(videoDiaryViewModel.state().isFlashEnabled)
        bindCamera(videoDiaryViewModel.state().isBackCameraToggled)

        taskFragment().toolbar.apply { hide() }

    }

    private fun bindRecordingState(state: RecordingState): Unit {

        val step = videoDiaryViewModel.state().step

        when (state) {
            RecordingState.Recording -> {

                recording_pause.isVisible = true
                review_pause.isEnabled = false

                video_view.isVisible = false

                flash_toggle.isVisible = videoDiaryViewModel.state().isBackCameraToggled
                camera_toggle.isVisible = false

                recording_pause.setImageResource(step.pauseImage)

                recording_pause.setOnClickListener {

                    startCoroutineAsync { videoDiaryViewModel.pause() }
                    pause()

                }

                record_info.isVisible = false
                review_info.isVisible = false

            }
            is RecordingState.RecordingPause -> {

                recording_pause.isVisible = true
                review_pause.isEnabled = false

                video_view.isVisible = false

                flash_toggle.isVisible = videoDiaryViewModel.state().isBackCameraToggled
                camera_toggle.isVisible = true

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
            RecordingState.Merged -> {

                recording_pause.isVisible = false
                review_pause.isEnabled = false
                flash_toggle.isVisible = false
                camera_toggle.isVisible = false

                review_loading.setVisibility(isVisible = true, opaque = false)

                mediaController.setMediaPlayer(video_view)
                mediaController.setAnchorView(video_view)
                video_view.setMediaController(mediaController)
                video_view.isVisible = true
                video_view.setVideoPath(getVideoMergeFilePath())
                video_view.setOnPreparedListener {
                    review_loading.setVisibility(isVisible = false, opaque = false)
                    it.isLooping = true
                    startCoroutineAsync { videoDiaryViewModel.reviewPause() }

                }
            }
            RecordingState.ReviewPause -> {

                recording_pause.isVisible = false
                review_pause.isEnabled = true
                flash_toggle.isVisible = false
                camera_toggle.isVisible = false
                video_view.isVisible = true
                camera.isVisible = false

                recording_title.text = step.startRecordingDescription

                val currentRecordTime =
                    DateUtils.formatElapsedTime(videoDiaryViewModel.state().recordTimeSeconds)
                val maxRecordTime =
                    DateUtils.formatElapsedTime(videoDiaryViewModel.state().maxRecordTimeSeconds)
                recording_title.text = step.startRecordingDescription

                val recordTimeLabel =
                    "$currentRecordTime/$maxRecordTime"

                review_time.text = recordTimeLabel


                mediaController.hide()
                mediaController.isVisible = false

                review_pause.setImageResource(step.playImage)
                review_pause.setOnClickListener {
                    startCoroutineAsync {
                        video_view.start()
                        videoDiaryViewModel.reviewPlay()
                    }
                }

                record_info.isVisible = false
                review_info.isVisible = true

            }
            RecordingState.Review -> {

                recording_pause.isVisible = false
                review_pause.isEnabled = true
                flash_toggle.isVisible = false
                camera_toggle.isVisible = false
                video_view.isVisible = true
                camera.isVisible = false

                recording_title.text = step.startRecordingDescription

                mediaController.isVisible = true
                mediaController.show()

                review_pause.setImageResource(step.pauseImage)
                review_pause.setOnClickListener {
                    startCoroutineAsync {
                        video_view.pause()
                        videoDiaryViewModel.reviewPause()
                    }
                }

                record_info.isVisible = false
                review_info.isVisible = false

            }
            RecordingState.Uploaded -> startCoroutineAsync { next() }
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

                title.text = recordTimeLabel

            }
            RecordingState.RecordingPause ->
                title.text = videoDiaryViewModel.state().step.title
            RecordingState.Review ->
                title.text = videoDiaryViewModel.state().step.title
        }
    }

    // TODO: check if flash is available
    private fun bindFlash(isFlashEnabled: Boolean): Unit {

        startCoroutine { Either.catch { camera.enableTorch(isFlashEnabled) } }

        flash_toggle.setImageResource(
            if (isFlashEnabled) videoDiaryViewModel.state().step.flashOnImage
            else videoDiaryViewModel.state().step.flashOffImage
        )

    }

    // TODO: check if camera lens is available
    private fun bindCamera(isBackCameraToggled: Boolean): Unit {

        startCoroutine {

            Either.catch {
                camera.cameraLensFacing =
                    if (isBackCameraToggled) CameraSelector.LENS_FACING_BACK
                    else CameraSelector.LENS_FACING_FRONT
            }

            // enable flash button only for back camera
            flash_toggle.isVisible = isBackCameraToggled
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

        videoDiaryViewModel.merge(
            getVideoDirectoryPath(),
            mergeDirectory.absolutePath,
            getVideoMergeFileName()
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

    private fun getVideoMergeFileName(): String =
        "merged_video_diary.mp4"

    private fun getVideoMergeFilePath(): String =
        "${getVideoMergeDirectoryPath()}/${getVideoMergeFileName()}"

    private fun getVideoMergeFile(): File = File(getVideoMergeFilePath())

    override fun onDestroy() {

        File(getVideoDirectoryPath()).deleteRecursively()

        super.onDestroy()
    }
}