package com.foryouandme.ui.compose.camera

import java.io.File

sealed class CameraEvent {

    data class Record(val file: File) : CameraEvent()
    object Pause : CameraEvent()

}