package com.foryouandme.researchkit.step.nineholepeg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.task.nineholepeg.NineHolePegAttempt
import com.foryouandme.entity.task.nineholepeg.NineHolePegPointPosition
import com.foryouandme.entity.task.nineholepeg.NineHolePegSubStep
import com.foryouandme.entity.task.nineholepeg.NineHolePegTargetPosition

@Composable
fun NineHolePegPoint(
    attempt: NineHolePegAttempt,
    pointSize: Dp = 100.dp,
    pointPadding: Dp = 30.dp,
    onDragStart: () -> Unit = { },
    onDragEnd: (Boolean) -> Unit = { },
) {

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        var startOffset by remember(attempt) {
            mutableStateOf(
                getOffsetByPoint(
                    attempt.step.start,
                    maxWidth,
                    pointSize,
                    pointPadding
                )
            )
        }

        val targetOffset by remember(attempt) {
            mutableStateOf(
                getOffsetByTarget(
                    attempt.step.target,
                    maxWidth,
                    pointSize,
                    pointPadding
                )
            )
        }


        var alpha by remember { mutableStateOf(1f) }

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
                    .pointerInput(attempt) {
                        detectGrabGestures(
                            {
                                onDragStart()
                                alpha = 0.5f
                            },
                            {
                                onDragEnd(
                                    isTargetReached(
                                        startOffset,
                                        targetOffset,
                                        attempt.step.target,
                                        pointSize
                                    )
                                )
                                alpha = 1f
                            },
                            {
                                onDragEnd(
                                    isTargetReached(
                                        startOffset,
                                        targetOffset,
                                        attempt.step.target,
                                        pointSize
                                    )
                                )
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
                                        attempt.step.target,
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
        NineHolePegTarget(attempt.step.target, targetOffset, pointSize)

    }

}

@Preview
@Composable
private fun NineHolePegPointPreview() {
    NineHolePegPoint(
        NineHolePegAttempt(
            0,
            NineHolePegSubStep(
                NineHolePegPointPosition.End,
                NineHolePegTargetPosition.StartCenter,
            ),
            0
        )
    )
}