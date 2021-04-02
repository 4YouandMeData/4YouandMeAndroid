package com.foryouandme.ui.auth.onboarding.step.video

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.core.ext.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.video.*
import kotlinx.android.synthetic.main.video_controls.*

@AndroidEntryPoint
class VideoFragment : OnboardingStepFragmentOld<VideoViewModel>(R.layout.video) {

    lateinit var player: SimpleExoPlayer

    override val viewModel: VideoViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { VideoViewModel(navigator) }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync { setupVideo() }

        if (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        configuration { applyConfiguration(it) }

    }

    private suspend fun setupVideo(): Unit =
        evalOnMain {

            player = SimpleExoPlayer.Builder(requireContext()).build()

            val media = buildRawMediaSource()

            media?.let {
                player.setMediaSource(it)
                player.prepare()
                player.playWhenReady = false
            }

            video.player = player

            fake_play.setOnClickListener {

                fake_play.isVisible = false
                next.isVisible = false
                video.useController = true
                video.showController()
                player.play()

            }

            player.addListener(object : Player.EventListener {

                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)

                    when (state) {

                        Player.STATE_ENDED ->
                            startCoroutineAsync { next() }

                        else -> {
                        }

                    }
                }

            })

        }

    private suspend fun applyConfiguration(config: Configuration): Unit =
        evalOnMain {

            setStatusBar(Color.BLACK)

            toolbar.showCloseSecondaryButton(imageConfiguration) {
                startCoroutineAsync { next() }
            }

            fake_play.setImageResource(imageConfiguration.videoDiaryPlay())

            exo_play.setImageResource(imageConfiguration.videoDiaryPlay())
            exo_pause.setImageResource(imageConfiguration.videoDiaryPause())

            next.text = config.text.onboarding.introVideoContinueButton
            next.setTextColor(config.theme.secondaryColor.color())
            next.background = button(config.theme.primaryColorEnd.color())

            next.setOnClickListenerAsync { next() }

        }

    private fun buildRawMediaSource(): MediaSource? {
        val rawDataSource = RawResourceDataSource(requireContext())
        // open the /raw resource file
        rawDataSource.open(
            DataSpec(
                RawResourceDataSource.buildRawResourceUri(
                    videoConfiguration.introVideo()
                )
            )
        )

        // Create media Item
        val mediaItem = MediaItem.fromUri(rawDataSource.uri!!)

        // create a media source with the raw DataSource

        return ProgressiveMediaSource.Factory { rawDataSource }.createMediaSource(mediaItem)
    }

    override fun onPause() {

        player.pause()

        super.onPause()

    }

    override fun onDestroyView() {

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        super.onDestroyView()

    }

    override fun onDestroy() {

        player.release()

        super.onDestroy()

    }

}