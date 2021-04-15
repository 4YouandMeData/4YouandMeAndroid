package com.foryouandme.entity.task.holepeg

sealed class HolePegTargetPosition {

    object Start: HolePegTargetPosition()
    object StartCenter: HolePegTargetPosition()
    object End: HolePegTargetPosition()
    object EndCenter: HolePegTargetPosition()

}