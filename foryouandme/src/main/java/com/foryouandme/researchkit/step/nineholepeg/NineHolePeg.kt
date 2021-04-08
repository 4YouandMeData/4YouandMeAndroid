package com.foryouandme.researchkit.step.nineholepeg

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Preview
@Composable
fun NineHolePeg() {

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }

    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier =
            Modifier
                .size(150.dp, 150.dp)
                .background(Color.Cyan)
                .pointerInput(Unit) {
                    detectGrabGestures { change, dragAmount ->
                        change.consumeAllChanges()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        ) {
            Box(
                modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.Black)
            )

        }

        /*Text(text = "drag me", modifier = Modifier
            .background(Color.Cyan)
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectGrabGestures { change, dragAmount ->
                    change.consumeAllChanges()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            })*/

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
                Log.d("PAN", zoom.toString())
                Log.d("CENTROID", event.calculateCentroidSize(useCurrent = true).toString())
                Log.d("CENTROID_PREV", event.calculateCentroidSize(useCurrent = false).toString())

            } while (event.changes.firstOrNull { it.pressed } != null && zoom >= 1f)

            Log.d("PAN", "END_GRAB: $zoom")
            if (zoom < 1f) {
                Log.d("PAN", "START_GRAB")
                do {
                    drag = awaitTouchSlopOrCancellation(down.id, onDrag)
                } while (drag != null && !drag.positionChangeConsumed())
                if (drag != null) {
                    onDragStart.invoke(drag.position)
                    if (
                        !drag(drag.id) {
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