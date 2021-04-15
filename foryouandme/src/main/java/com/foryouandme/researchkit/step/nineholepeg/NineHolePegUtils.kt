package com.foryouandme.researchkit.step.nineholepeg

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.task.nineholepeg.NineHolePegPointPosition
import com.foryouandme.entity.task.nineholepeg.NineHolePegTargetPosition
import kotlin.math.abs

internal fun getOffsetByPoint(
    point: NineHolePegPointPosition,
    width: Dp,
    pointSize: Dp,
    padding: Dp
): DpOffset {

    val startX = (-width / 2) + (pointSize / 2) + padding
    val endX = (width / 2) - (pointSize / 2) - padding

    return when (point) {
        NineHolePegPointPosition.Center -> DpOffset(0.dp, 0.dp)
        NineHolePegPointPosition.End -> DpOffset(endX, 0.dp)
        NineHolePegPointPosition.Start -> DpOffset(startX, 0.dp)
    }

}

internal fun getOffsetByTarget(
    point: NineHolePegTargetPosition,
    width: Dp,
    pointSize: Dp,
    padding: Dp
): DpOffset {

    return when (point) {
        NineHolePegTargetPosition.End ->
            DpOffset(
                (width / 2) - (pointSize) - (padding),
                0.dp
            )
        NineHolePegTargetPosition.EndCenter ->
            DpOffset(
                (width / 2) - (pointSize / 2) - padding,
                0.dp
            )
        NineHolePegTargetPosition.Start ->
            DpOffset(
                (-width / 2) + (pointSize) + (padding),
                0.dp
            )
        NineHolePegTargetPosition.StartCenter ->
            DpOffset(
                (-width / 2) + (pointSize / 2) + padding,
                0.dp
            )
    }

}

internal fun isTargetReached(
    pointOffset: DpOffset,
    targetOffset: DpOffset,
    targetPoint: NineHolePegTargetPosition,
    pointSize: Dp
): Boolean =
    when (targetPoint) {
        NineHolePegTargetPosition.End -> (pointOffset.x - (pointSize / 2)) > targetOffset.x
        NineHolePegTargetPosition.Start -> (pointOffset.x + (pointSize / 2)) < targetOffset.x
        NineHolePegTargetPosition.EndCenter,
        NineHolePegTargetPosition.StartCenter -> {

            val offsetDiff = pointOffset - targetOffset
            abs(offsetDiff.x.value) < 10 && abs(offsetDiff.y.value) < 10

        }
    }
