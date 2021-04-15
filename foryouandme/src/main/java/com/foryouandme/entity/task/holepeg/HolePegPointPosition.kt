package com.foryouandme.entity.task.holepeg

sealed class HolePegPointPosition {

    object Center: HolePegPointPosition()
    object Start: HolePegPointPosition()
    object End: HolePegPointPosition()

}