package com.foryouandme.researchkit.step.nineholepeg

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun NineHolePegTarget(
    targetOffset: DpOffset = DpOffset(0.dp, 0.dp),
    pointSize: Dp = 100.dp,
    pointPadding: Dp = 30.dp,
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .offset(targetOffset.x, targetOffset.y)

    ) {
        Box(
            modifier = Modifier
                .size(pointSize)
                .border(3.dp, Color.Black, CircleShape)
                .background(Color.Transparent)
        )
    }

}