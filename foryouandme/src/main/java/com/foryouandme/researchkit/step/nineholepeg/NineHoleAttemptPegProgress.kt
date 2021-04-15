package com.foryouandme.researchkit.step.nineholepeg

import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.foryouandme.entity.task.nineholepeg.NineHolePegAttempt
import com.foryouandme.entity.task.nineholepeg.NineHolePegPointPosition
import com.foryouandme.entity.task.nineholepeg.NineHolePegSubStep
import com.foryouandme.entity.task.nineholepeg.NineHolePegTargetPosition

@Composable
fun NineHoleAttemptPegProgress(
    attempt: NineHolePegAttempt,
    color: Color,
    modifier: Modifier = Modifier
) {

    LinearProgressIndicator(
        progress = getProgress(attempt),
        color = color,
        modifier = modifier
    )

}

private fun getProgress(attempt: NineHolePegAttempt) =
    attempt.peg.toFloat() / (attempt.totalPegs.toFloat() + 1)

@Preview
@Composable
fun NineHoleAttemptPegProgressPreview() {

    NineHoleAttemptPegProgress(
        NineHolePegAttempt(
            0,
            NineHolePegSubStep(NineHolePegPointPosition.End, NineHolePegTargetPosition.Start),
            0,
            5,
            9
        ),
        Color.Magenta,
    )

}