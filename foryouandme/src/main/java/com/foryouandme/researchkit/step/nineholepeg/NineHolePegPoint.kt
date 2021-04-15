package com.foryouandme.researchkit.step.nineholepeg

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Preview
@Composable
fun NineHolePegPoint(
    startPoint: NineHolePegPointPosition = NineHolePegPointPosition.End,
    targetPoint: NineHolePegTargetPosition = NineHolePegTargetPosition.StartCenter,
    pointSize: Dp = 100.dp,
    pointPadding: Dp = 30.dp,
    onDragStart: () -> Unit = { },
    onDragEnd: () -> Unit = { },
) {

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        var startOffset by remember(startPoint, maxWidth, pointSize, pointPadding) {
            mutableStateOf(
                getOffsetByPoint(
                    startPoint,
                    maxWidth,
                    pointSize,
                    pointPadding
                )
            )
        }

        val targetOffset by remember(startPoint, maxWidth, pointSize, pointPadding) {
            mutableStateOf(
                getOffsetByTarget(
                    targetPoint,
                    maxWidth,
                    pointSize,
                    pointPadding
                )
            )
        }

        var alpha by remember { mutableStateOf(1f) }

        // Draggable Point
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .offset(startOffset.x, startOffset.y)

        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier =
                Modifier
                    .size((pointSize + 100.dp), (pointSize + 200.dp))
                    .background(Color.Cyan)
                    .pointerInput(Unit) {
                        detectGrabGestures(
                            {
                                onDragStart()
                                alpha = 0.5f
                            },
                            {
                                onDragEnd()
                                alpha = 1f
                            },
                            {
                                onDragEnd()
                                alpha = 1f
                            }
                        ) { change, dragAmount ->
                            change.consumeAllChanges()

                            val offsetX = startOffset.x + dragAmount.x.toDp()
                            val offsetY = startOffset.y + dragAmount.y.toDp()

                            startOffset = DpOffset(offsetX, offsetY)
                            alpha =
                                if (
                                    isTargetReached(
                                        startOffset,
                                        targetOffset,
                                        targetPoint,
                                        pointSize
                                    )
                                ) 1f
                                else 0.5f
                        }
                    }
                    .alpha(alpha)
            ) {
                Box(
                    modifier = Modifier
                        .size(pointSize)
                        .clip(CircleShape)
                        .background(Color.Black)
                )
            }

        }

        // Target Point
        NineHolePegTarget(targetPoint, targetOffset, pointSize, pointPadding)

    }

}

suspend fun PointerInputScope.detectGrabGestures(
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit
) {
    forEachGesture {
        awaitPointerEventScope {

            val down = awaitFirstDown(requireUnconsumed = false)
            var drag: PointerInputChange?
            var zoom = 1f

            do {

                val event = awaitPointerEvent()
                val zoomChange = event.calculateZoom()
                zoom *= zoomChange

            } while (event.changes.firstOrNull { it.pressed } != null && zoom >= 1f)

            if (zoom < 1f) {
                do {
                    drag = awaitTouchSlopOrCancellation(down.id, onDrag)
                } while (drag != null && !drag.positionChangeConsumed())
                if (drag != null) {
                    onDragStart.invoke(drag.position)
                    if (
                        !dragTwoFinger(drag.id) {
                            onDrag(it, it.positionChange())
                        }
                    ) {
                        onDragCancel()
                    } else {
                        onDragEnd()
                    }
                }
            }
        }
    }
}

suspend fun AwaitPointerEventScope.dragTwoFinger(
    pointerId: PointerId,
    onDrag: (PointerInputChange) -> Unit
): Boolean {
    var pointer = pointerId
    while (true) {
        val change = awaitDragTwoFingerOrCancellation(pointer) ?: return false

        if (change.changedToUpIgnoreConsumed()) {
            return true
        }

        onDrag(change)
        pointer = change.id
    }
}

suspend fun AwaitPointerEventScope.awaitDragTwoFingerOrCancellation(
    pointerId: PointerId,
): PointerInputChange? {
    if (currentEvent.arePointersUp(pointerId)) {
        return null // The pointer has already been lifted, so the gesture is canceled
    }
    val change = awaitDragOrUp(pointerId) { it.positionChangedIgnoreConsumed() }
    return if (change.positionChangeConsumed()) null else change
}

private suspend inline fun AwaitPointerEventScope.awaitDragOrUp(
    pointerId: PointerId,
    hasDragged: (PointerInputChange) -> Boolean
): PointerInputChange {
    var pointer = pointerId
    while (true) {
        val event = awaitPointerEvent()
        val dragEvent = event.changes.firstOrNull { it.id == pointer }!!
        if (dragEvent.changedToUpIgnoreConsumed()) {
            val otherDown = event.changes.firstOrNull { it.pressed }
            if (otherDown == null) {
                // This is the last "up"
                return dragEvent
            } else {
                pointer = otherDown.id
            }
        } else if (hasDragged(dragEvent)) {
            return dragEvent
        }
    }
}

private fun PointerEvent.arePointersUp(pointerId: PointerId): Boolean =
    changes.firstOrNull { it.id == pointerId }?.pressed != true ||
            changes.filter { it.pressed }.size != 2

private fun getOffsetByPoint(
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

private fun getOffsetByTarget(
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

private fun isTargetReached(
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
