package com.foryouandme.auth.video

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.auth.AuthSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.button.button
import com.foryouandme.core.ext.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import kotlinx.android.synthetic.main.video.*
import kotlinx.android.synthetic.main.video_controls.*

class VideoFragment : AuthSectionFragment<VideoViewModel>() {

    private var player: SimpleExoPlayer? = null

    private var currentView: View? = null

    override val viewModel: VideoViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { VideoViewModel(navigator) }
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (currentView == null) {
            currentView = inflater.inflate(R.layout.video, container, false)
        }

        return currentView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            val restore =
                savedInstanceState?.let {
                    mapNotNull(
                        it.getString(IS_PLAYING, null).toBoolean(),
                        it.getString(POSITION, null).toLongOrNull()
                    )
                }?.let { (a, b) -> VideoRestore(a, b) }

            setupVideo(restore)

        }

        if (requireActivity().requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        configuration { applyConfiguration(it) }

    }

    private suspend fun setupVideo(restore: VideoRestore?): Unit =
        evalOnMain {

            if (player == null) {
                player = SimpleExoPlayer.Builder(requireContext()).build()
                player?.let { simpleExoPlayer ->

                    val media = buildRawMediaSource()

                    media?.let {
                        simpleExoPlayer.setMediaSource(it)
                        simpleExoPlayer.prepare()
                        simpleExoPlayer.playWhenReady = false
                    }

                }
            }

            video.player = player

            player?.let { simpleExoPlayer ->

                if (restore == null) {

                    fake_play.setOnClickListener {

                        fake_play.isVisible = false
                        next.isVisible = false
                        video.useController = true
                        video.showController()
                        simpleExoPlayer.play()

                    }

                    next.isVisible = simpleExoPlayer.isPlaying.not()

                } else {

                    fake_play.isVisible = false
                    next.isVisible = false
                    video.useController = true
                    video.showController()
                    simpleExoPlayer.seekTo(restore.position)
                    if (restore.isPlaying)
                        simpleExoPlayer.play()

                }

            }

        }

    private suspend fun applyConfiguration(config: Configuration): Unit =
        evalOnMain {

            setStatusBar(Color.BLACK)

            toolbar.showCloseButton(imageConfiguration) {
                startCoroutineAsync { viewModel.screening(authNavController()) }
            }

            fake_play.setImageResource(imageConfiguration.videoDiaryPlay())

            exo_play.setImageResource(imageConfiguration.videoDiaryPlay())
            exo_pause.setImageResource(imageConfiguration.videoDiaryPause())

            next.text = config.text.onboarding.introVideoContinueButton
            next.setTextColor(config.theme.secondaryColor.color())
            next.background = button(config.theme.primaryColorEnd.color())

            next.setOnClickListenerAsync { viewModel.screening(authNavController()) }

        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(IS_PLAYING, player?.isPlaying?.toString())
        outState.putString(POSITION, player?.currentPosition?.toString())
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

        //player?.pause()

        super.onPause()

    }

    override fun onDestroyView() {

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        super.onDestroyView()

    }

    override fun onDestroy() {

        player?.release()
        player = null

        super.onDestroy()

    }

    data class VideoRestore(val isPlaying: Boolean, val position: Long)

    companion object {

        private const val IS_PLAYING = "is_playing"

        private const val POSITION = "position"

    }

}