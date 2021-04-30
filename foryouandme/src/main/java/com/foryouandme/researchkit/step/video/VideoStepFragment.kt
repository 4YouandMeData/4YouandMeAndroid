package com.foryouandme.researchkit.step.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.video.compose.VideoStepPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoStepFragment : StepFragment() {

    private val viewModel: VideoStepViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
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

}