package com.foryouandme.ui.auth.onboarding.step.video

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.core.ext.*
import com.foryouandme.databinding.VideoBinding
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepFragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoFragment : OnboardingStepFragment(R.layout.video) {

    lateinit var player: SimpleExoPlayer

    private val binding: VideoBinding?
        get() = view?.let { VideoBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVideo()

        if (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        applyConfiguration()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyConfiguration()
    }

    private fun setupVideo() {

        val viewBinding = binding

        if (viewBinding != null) {

            player = SimpleExoPlayer.Builder(requireContext()).build()

            val media = buildRawMediaSource()

            media.let {
                player.setMediaSource(it)
                player.prepare()
                player.playWhenReady = false
            }

            viewBinding.video.player = player

            viewBinding.fakePlay.setOnClickListener {

                viewBinding.fakePlay.isVisible = false
                viewBinding.next.isVisible = false
                viewBinding.video.useController = true
                viewBinding.video.showController()
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
    }

    private fun applyConfiguration() {

        val config = configuration
        val viewBinding = binding

        if (config != null && viewBinding != null) {

            setStatusBar(Color.BLACK)

            viewBinding.toolbar.showCloseSecondaryButton(imageConfiguration) { next() }

            viewBinding.fakePlay.setImageResource(imageConfiguration.videoDiaryPlay())

            viewBinding.root.findViewById<ImageView>(R.id.exo_play)
                .setImageResource(imageConfiguration.videoDiaryPlay())

            viewBinding.root.findViewById<ImageView>(R.id.exo_pause)
                .setImageResource(imageConfiguration.videoDiaryPause())

            viewBinding.next.text = config.text.onboarding.introVideoContinueButton
            viewBinding.next.setTextColor(config.theme.secondaryColor.color())
            viewBinding.next.background = button(config.theme.primaryColorEnd.color())

            viewBinding.next.setOnClickListener { next() }

        }
    }

    private fun buildRawMediaSource(): MediaSource {

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