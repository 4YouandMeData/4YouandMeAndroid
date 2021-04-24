package com.foryouandme.entity.camera

import java.io.File

sealed class CameraLens {

    object Front : CameraLens()
    object Back : CameraLens()

}

sealed class CameraFlash {

    object On : CameraFlash()
    object Off : CameraFlash()

}

sealed class CameraEvent {

    data class Record(val file: File) : CameraEvent()
    object Pause : CameraEvent()

}