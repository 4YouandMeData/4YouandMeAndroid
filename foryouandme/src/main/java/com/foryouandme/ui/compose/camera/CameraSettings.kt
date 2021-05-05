package com.foryouandme.ui.compose.camera

sealed class CameraLens {

    object Front : CameraLens()
    object Back : CameraLens()

}

sealed class CameraFlash {

    object On : CameraFlash()
    object Off : CameraFlash()

}

data class VideoSettings(
    val targetResolution: Resolution? = null,
    val bitrate: Int? = null,
    val frameRate: Int? = null
)

data class Resolution(val width: Int, val height: Int)