package com.foryouandme.auth.video

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.navigator
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.core.ext.videoConfiguration
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import kotlinx.android.synthetic.main.video.*

class VideoFragment : BaseFragment<VideoViewModel>(R.layout.video) {

    private lateinit var player: SimpleExoPlayer

    override val viewModel: VideoViewModel by lazy {

        viewModelFactory(
            this,
            getFactory { VideoViewModel(navigator) }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync { setupVideo() }

    }

    private suspend fun setupVideo(): Unit =
        evalOnMain {

            player = SimpleExoPlayer.Builder(requireContext()).build()
            video.player = player

            val media = buildRawMediaSource()

            media?.let {
                player.setMediaSource(it)
                player.prepare()
                player.playWhenReady = false
            }

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

    override fun onDestroyView() {

        player.release()

        super.onDestroyView()
    }
}