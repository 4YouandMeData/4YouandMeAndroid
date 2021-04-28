package com.foryouandme.ui.compose.video

sealed class VideoPlayerEvent {

    object Play: VideoPlayerEvent()

    object Pause: VideoPlayerEvent()

}
