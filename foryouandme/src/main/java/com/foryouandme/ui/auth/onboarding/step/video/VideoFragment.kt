package com.foryouandme.ui.auth.onboarding.step.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.core.arch.deps.VideoConfiguration
import com.foryouandme.core.ext.*
import com.foryouandme.databinding.VideoBinding
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepFragment
import com.foryouandme.ui.auth.onboarding.step.video.compose.Video
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoFragment : OnboardingStepFragment() {

    private val viewModel by viewModels<VideoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                Video(viewModel = viewModel, next = { next() })
            }
        }

}