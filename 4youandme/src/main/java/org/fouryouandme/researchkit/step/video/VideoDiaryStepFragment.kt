package org.fouryouandme.researchkit.step.video

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.camera.core.VideoCapture
import androidx.camera.view.CameraView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import arrow.fx.IO
import arrow.fx.extensions.fx
import kotlinx.android.synthetic.main.step_video_diary.*
import kotlinx.android.synthetic.main.task.*
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.R
import org.fouryouandme.core.ext.hide
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepFragment
import java.io.File


class VideoDiaryStepFragment : StepFragment(R.layout.step_video_diary) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        video_view.isVisible = false

        val step =
            viewModel.getStepByIndexAs<Step.VideoDiaryStep>(indexArg())

        step.map { applyData(it) }


    }

    private fun applyData(
        step: Step.VideoDiaryStep
    ): Unit {

        camera.bindToLifecycle(this)
        camera.captureMode = CameraView.CaptureMode.VIDEO

        val directory =
            File(
                "${requireContext().applicationContext.filesDir.absolutePath}/video-diary"
            )

        if (!directory.exists())
            directory.mkdir()

        record.setOnClickListener {

            val file = File(directory, "${System.currentTimeMillis()}.mp4")

            camera.startRecording(
                file,
                ContextCompat.getMainExecutor(requireContext()),
                object : VideoCapture.OnVideoSavedCallback {

                    override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {

                        val i = 0

                    }

                    override fun onError(
                        videoCaptureError: Int,
                        message: String,
                        cause: Throwable?
                    ) {

                        val i = 0
                    }

                }
            )
        }

        stop.setOnClickListener {
            camera.stopRecording()
        }

        merge.setOnClickListener { playVideo() }

        taskFragment().toolbar.apply {

            hide()

        }

    }

    private fun playVideo() {

        camera.isVisible = false
        record.isVisible = false
        stop.isVisible = false
        video_view.isVisible = true
        merge.isVisible = false


        val directory =
            File(
                "${requireContext().applicationContext.filesDir.absolutePath}/video-diary/merge"
            )

        if (!directory.exists())
            directory.mkdir()

        IO.fx {

            val output =
                !mergeVideoDiary(
                    "${requireContext().applicationContext.filesDir.absolutePath}/video-diary/",
                    directory.absolutePath
                )

            val uri = Uri.parse(output)
            continueOn(Dispatchers.Main)
            video_view.setVideoURI(uri)
            video_view.setOnPreparedListener { video_view.start() }

        }.unsafeRunAsync()

    }

    fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory)
            fileOrDirectory.listFiles()?.forEach { deleteRecursive(it) }
        fileOrDirectory.delete()
    }

    override fun onDestroy() {

        val directory =
            File("${requireContext().applicationContext.filesDir.absolutePath}/video-diary/")
        deleteRecursive(directory)


        super.onDestroy()
    }

}