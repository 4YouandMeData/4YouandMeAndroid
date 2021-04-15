package com.foryouandme.researchkit.step.holepeg

import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.foryouandme.entity.task.holepeg.HolePegAttempt
import com.foryouandme.entity.task.holepeg.HolePegPointPosition
import com.foryouandme.entity.task.holepeg.HolePegSubStep
import com.foryouandme.entity.task.holepeg.HolePegTargetPosition

@Composable
fun HoleAttemptPegProgress(
    attempt: HolePegAttempt,
    color: Color,
    modifier: Modifier = Modifier
) {

    LinearProgressIndicator(
        progress = getProgress(attempt),
        color = color,
        modifier = modifier
    )

}

private fun getProgress(attempt: HolePegAttempt) =
    attempt.peg.toFloat() / (attempt.totalPegs.toFloat() + 1)

@Preview
@Composable
fun HoleAttemptPegProgressPreview() {

    HoleAttemptPegProgress(
        HolePegAttempt(
            0,
            HolePegSubStep(HolePegPointPosition.End, HolePegTargetPosition.Start),
            0,
            5,
            9
        ),
        Color.Magenta,
    )

}