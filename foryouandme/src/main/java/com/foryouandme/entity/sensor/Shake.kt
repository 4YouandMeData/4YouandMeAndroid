package com.foryouandme.entity.sensor

data class Shake(val sensitivity: ShakeSensitivity)

sealed class ShakeSensitivity {

    object Light : ShakeSensitivity()
    object Medium : ShakeSensitivity()
    object Hard : ShakeSensitivity()

}