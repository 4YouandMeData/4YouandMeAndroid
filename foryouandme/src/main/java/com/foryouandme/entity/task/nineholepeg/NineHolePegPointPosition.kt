package com.foryouandme.entity.task.nineholepeg

sealed class NineHolePegPointPosition {

    object Center: NineHolePegPointPosition()
    object Start: NineHolePegPointPosition()
    object End: NineHolePegPointPosition()

}