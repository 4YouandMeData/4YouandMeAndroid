package com.foryouandme.ui.auth.onboarding.step.video.compose

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.foryouandme.R
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.VideoConfiguration
import com.foryouandme.core.ext.findActivity
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.auth.onboarding.step.video.VideoAction.*
import com.foryouandme.ui.auth.onboarding.step.video.VideoState
import com.foryouandme.ui.auth.onboarding.step.video.VideoViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.video.VideoPlayer

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun Video(
    viewModel: VideoViewModel,
    next: () -> Unit = {}
) {

    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    DisposableEffect(key1 = "video") {

        val activity = context.findActivity()
        if (activity != null) {
            if (activity.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { viewModel.execute(GetConfiguration) }
    ) {
        Video(
            state = state,
            configuration = it,
            imageConfiguration = viewModel.imageConfiguration,
            videoConfiguration = viewModel.videoConfiguration,
            onNext = next,
            onPlay = { viewModel.execute(Play) },
            onPause = { viewModel.execute(Pause) }
        )
    }

}

@Composable
private fun Video(
    state: VideoState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    videoConfiguration: VideoConfiguration,
    onNext: () -> Unit = {},
    onPlay: () -> Unit = {},
    onPause: () -> Unit = {}
) {
    StatusBar(color = Color.Black)
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .background(configuration.theme.primaryColorStart.value)
    ) {
        VideoPlayer(
            resource = videoConfiguration.introVideo(),
            isPlaying = state.isPlaying,
            usePlayerController = state.isPlaying,
            playerLayoutId = R.layout.video,
            playerResizeMode = null,
            loop = false,
            onEnd = onNext,
            configurePlayer = {
                it.findViewById<ImageView>(R.id.exo_play)
                    .apply {
                        setImageResource(imageConfiguration.videoDiaryPlay())
                        setOnClickListener { onPlay() }
                    }
                it.findViewById<ImageView>(R.id.exo_pause).apply {
                    setImageResource(imageConfiguration.videoDiaryPause())
                    setOnClickListener { onPause() }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        ForYouAndMeTopAppBar(
            imageConfiguration = imageConfiguration,
            icon = TopAppBarIcon.CloseSecondary,
            modifier = Modifier.fillMaxWidth(),
            onBack = onNext
        )
        if (state.isPlaying.not())
            Image(
                painter = painterResource(id = imageConfiguration.videoDiaryPlay()),
                contentDescription = null,
                modifier =
                Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
                    .clickable { onPlay() }
            )
        if (state.isPlaying.not())
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
            ) {
                ForYouAndMeButton(
                    text = configuration.text.onboarding.introVideoContinueButton,
                    backgroundColor = configuration.theme.primaryColorEnd.value,
                    textColor = configuration.theme.secondaryColor.value,
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth()
                )
            }
    }
}