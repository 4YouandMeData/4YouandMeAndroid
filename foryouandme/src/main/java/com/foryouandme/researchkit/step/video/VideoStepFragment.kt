package com.foryouandme.researchkit.step.video

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.video.compose.VideoStepPage
import dagger.hilt.android.AndroidEntryPoint
import jp.co.cyberagent.android.gpuimage.GPUImageView

@AndroidEntryPoint
class VideoStepFragment : StepFragment() {

    private val viewModel: VideoStepViewModel by viewModels()
    private var previewDisplayView: GPUImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            previewDisplayView = GPUImageView(requireContext());
            setupPreviewDisplayView(container);
            setContent {
                VideoStepPage(
                    onCloseClicked = { showCancelDialog() },
                    onVideoSubmitted = { next() },
                    close = { close() },
                    taskId = taskViewModel.state.task?.id.orEmpty()
                )
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = taskViewModel.getStepByIndexAs<VideoStep>(indexArg())
        viewModel.execute(VideoStepAction.SetStep(step))

    }

    // Initialization for Surface View
    // This can be used to manipulate Camera
    private fun setupPreviewDisplayView(viewGroup: ViewGroup?) {
        previewDisplayView!!.visibility = View.VISIBLE
        viewGroup?.addView(previewDisplayView)

    }

}