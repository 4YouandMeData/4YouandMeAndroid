package com.foryouandme.researchkit.step.holepeg

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.task.holepeg.HolePegPointPosition
import com.foryouandme.entity.task.holepeg.HolePegTargetPosition
import kotlin.math.abs

internal fun getOffsetByPoint(
    point: HolePegPointPosition,
    width: Dp,
    pointSize: Dp,
    padding: Dp
): DpOffset {

    val startX = (-width / 2) + (pointSize / 2) + padding
    val endX = (width / 2) - (pointSize / 2) - padding

    return when (point) {
        HolePegPointPosition.Center -> DpOffset(0.dp, 0.dp)
        HolePegPointPosition.End -> DpOffset(endX, 0.dp)
        HolePegPointPosition.Start -> DpOffset(startX, 0.dp)
    }

}

internal fun getOffsetByTarget(
    point: HolePegTargetPosition,
    width: Dp,
    pointSize: Dp,
    padding: Dp
): DpOffset {

    return when (point) {
        HolePegTargetPosition.End ->
            DpOffset(
                (width / 2) - (pointSize) - (padding),
                0.dp
            )
        HolePegTargetPosition.EndCenter ->
            DpOffset(
                (width / 2) - (pointSize / 2) - padding,
                0.dp
            )
        HolePegTargetPosition.Start ->
            DpOffset(
                (-width / 2) + (pointSize) + (padding),
                0.dp
            )
        HolePegTargetPosition.StartCenter ->
            DpOffset(
                (-width / 2) + (pointSize / 2) + padding,
                0.dp
            )
    }

}

internal fun isTargetReached(
    pointOffset: DpOffset,
    targetOffset: DpOffset,
    targetPoint: HolePegTargetPosition,
    pointSize: Dp
): Boolean =
    when (targetPoint) {
        HolePegTargetPosition.End -> (pointOffset.x - (pointSize / 2)) > targetOffset.x
        HolePegTargetPosition.Start -> (pointOffset.x + (pointSize / 2)) < targetOffset.x
        HolePegTargetPosition.EndCenter,
        HolePegTargetPosition.StartCenter -> {

            val offsetDiff = pointOffset - targetOffset
            abs(offsetDiff.x.value) < 10 && abs(offsetDiff.y.value) < 10

        }
    }
