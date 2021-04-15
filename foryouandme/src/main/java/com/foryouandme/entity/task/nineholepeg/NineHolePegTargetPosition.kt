package com.foryouandme.entity.task.nineholepeg

sealed class NineHolePegTargetPosition {

    object Start: NineHolePegTargetPosition()
    object StartCenter: NineHolePegTargetPosition()
    object End: NineHolePegTargetPosition()
    object EndCenter: NineHolePegTargetPosition()

}