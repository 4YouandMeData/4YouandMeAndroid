package com.foryouandme.ui.compose.video

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.foryouandme.core.ext.catchToNull
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun VideoPlayer(
    resource: Int,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    usePlayerController: Boolean = false,
    loop: Boolean = false,
    playerLayoutId: Int? = null,
    playerResizeMode: Int? = AspectRatioFrameLayout.RESIZE_MODE_ZOOM,
    configurePlayer: (View) -> Unit = {},
    onEnd: () -> Unit = {}
) {

    val context = LocalContext.current
    val mediaSource = remember {

        val rawDataSource = RawResourceDataSource(context)
        // open the /raw resource file
        rawDataSource.open(DataSpec(RawResourceDataSource.buildRawResourceUri(resource)))

        // Create media Item
        val mediaItem = MediaItem.fromUri(rawDataSource.uri!!)

        // create a media source with the raw DataSource
        ProgressiveMediaSource.Factory { rawDataSource }.createMediaSource(mediaItem)
    }

    VideoPlayer(
        mediaSource = mediaSource,
        isPlaying = isPlaying,
        usePlayerController = usePlayerController,
        loop = loop,
        playerLayoutId = playerLayoutId,
        playerResizeMode = playerResizeMode,
        configurePlayer = configurePlayer,
        onEnd = onEnd,
        modifier = modifier
    )

}

@Composable
fun VideoPlayer(
    mediaSource: ProgressiveMediaSource,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    usePlayerController: Boolean = false,
    playerLayoutId: Int? = null,
    playerResizeMode: Int? = AspectRatioFrameLayout.RESIZE_MODE_ZOOM,
    loop: Boolean = false,
    configurePlayer: (View) -> Unit = {},
    onEnd: () -> Unit = {}
) {

    val context = LocalContext.current

    // Do not recreate the player everytime this Composable commits
    val exoPlayer = remember { SimpleExoPlayer.Builder(context).build() }

    LaunchedEffect(mediaSource) {

        exoPlayer.setMediaSource(mediaSource)
        if (loop) exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
        exoPlayer.prepare()

    }

    LaunchedEffect(key1 = isPlaying) {
        if (isPlaying != exoPlayer.isPlaying) {
            if (isPlaying) exoPlayer.play() else exoPlayer.pause()
        }
    }

    AndroidView(
        factory = { ctx ->
            val view =
                if (playerLayoutId != null)
                    LayoutInflater.from(ctx).inflate(
                        playerLayoutId,
                        null,
                        false
                    ) as PlayerView
                else
                    PlayerView(ctx)
            view.apply {
                player = exoPlayer
                if (playerResizeMode != null) resizeMode = playerResizeMode
                useController = usePlayerController
                exoPlayer.addListener(object : Player.EventListener {

                    override fun onPlaybackStateChanged(state: Int) {
                        super.onPlaybackStateChanged(state)

                        when (state) {

                            Player.STATE_ENDED -> onEnd()
                            else -> Unit

                        }
                    }

                })
                configurePlayer(this)
            }

            view
        },
        update = {
            if (it.useController != usePlayerController)
                it.useController = usePlayerController
        },
        modifier = modifier.background(Color.Black)
    )

    DisposableEffect(key1 = "video_player") {

        onDispose {
            catchToNull { exoPlayer.stop() }
            catchToNull { exoPlayer.release() }
        }

    }

}

@Composable
fun VideoPlayer(
    sourceUrl: String,
    videoPlayerEvents: Flow<VideoPlayerEvent>,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    // Do not recreate the player everytime this Composable commits
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    // We only want to react to changes in sourceUrl.
    // This effect will be executed at each commit phase if
    // [sourceUrl] has changed.
    LaunchedEffect(sourceUrl) {

        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, context.packageName)
            )

        val source =
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(sourceUrl)))

        exoPlayer.setMediaSource(source)
        exoPlayer.repeatMode = Player.REPEAT_MODE_ALL
        exoPlayer.prepare()

    }

    // Gateway to traditional Android Views
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                useController = false
            }
        },
        modifier = modifier.background(Color.Black)
    )

    LaunchedEffect(key1 = videoPlayerEvents) {
        videoPlayerEvents.onEach {
            when (it) {
                VideoPlayerEvent.Pause -> exoPlayer.pause()
                VideoPlayerEvent.Play -> exoPlayer.play()
            }
        }.collect()
    }

    DisposableEffect(key1 = "video_player") {

        onDispose {
            catchToNull { exoPlayer.stop() }
            catchToNull { exoPlayer.release() }
        }

    }

}