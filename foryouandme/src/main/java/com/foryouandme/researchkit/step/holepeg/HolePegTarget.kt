package com.foryouandme.researchkit.step.holepeg

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.task.holepeg.HolePegTargetPosition

@Preview
@Composable
fun HolePegTarget(
    targetPosition: HolePegTargetPosition = HolePegTargetPosition.StartCenter,
    targetColor: Color = Color.Black,
    targetOffset: DpOffset = DpOffset(0.dp, 0.dp),
    pointSize: Dp = 100.dp,
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .offset(targetOffset.x, targetOffset.y)

    ) {
        when (targetPosition) {
            HolePegTargetPosition.End,
            HolePegTargetPosition.Start ->
                HolePegLineTarget(targetColor)
            HolePegTargetPosition.EndCenter,
            HolePegTargetPosition.StartCenter ->
                HolePegCircleTarget(targetColor, pointSize)
        }
    }

}

@Preview
@Composable
private fun HolePegCircleTarget(targetColor: Color = Color.Black, pointSize: Dp = 100.dp) {
    Box(
        modifier = Modifier
            .size(pointSize)
            .border(3.dp, targetColor, CircleShape)
            .background(Color.Transparent)
    )
}

@Preview
@Composable
private fun HolePegLineTarget(targetColor: Color = Color.Black) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            start = Offset(x = canvasWidth / 2, y = 0f),
            end = Offset(x = canvasWidth / 2, y = canvasHeight),
            color = targetColor,
            strokeWidth = 5F,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )

    }
}