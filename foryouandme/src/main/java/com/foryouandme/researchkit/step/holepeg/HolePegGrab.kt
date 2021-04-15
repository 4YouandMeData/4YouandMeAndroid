package com.foryouandme.researchkit.step.holepeg

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*

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