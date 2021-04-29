package com.foryouandme.ui.compose.camera

sealed class CameraLens {

    object Front : CameraLens()
    object Back : CameraLens()

}

sealed class CameraFlash {

    object On : CameraFlash()
    object Off : CameraFlash()

}