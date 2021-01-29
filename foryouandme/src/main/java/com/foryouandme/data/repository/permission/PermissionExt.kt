package com.foryouandme.data.repository.permission

import android.Manifest
import com.foryouandme.entity.permission.Permission

val Permission.name: String
    get() = when (this) {
        Permission.Camera -> Manifest.permission.CAMERA
        Permission.RecordAudio -> Manifest.permission.RECORD_AUDIO
        Permission.Location -> Manifest.permission.ACCESS_FINE_LOCATION
    }

fun Permission.Companion.fromName(name: String): Permission? =
    when (name) {

        Permission.Camera.name -> Permission.Camera
        Permission.RecordAudio.name -> Permission.RecordAudio
        Permission.Location.name -> Permission.Location
        else -> null

    }