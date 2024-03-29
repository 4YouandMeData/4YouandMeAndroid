package com.foryouandme.researchkit.step.holepeg

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.R
import com.foryouandme.entity.task.holepeg.HolePegPointPosition
import com.foryouandme.entity.task.holepeg.HolePegSubStep
import com.foryouandme.entity.task.holepeg.HolePegTargetPosition
import com.foryouandme.researchkit.step.Block
import com.foryouandme.researchkit.step.compose.StepHeader
import com.foryouandme.researchkit.step.holepeg.HolePegAction.*
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.toColor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun HolePeg(
    viewModel: HolePegViewModel = viewModel(),
    onComplete: (HolePegState) -> Unit = {}
) {

    ForYouAndMeTheme {

        LaunchedEffect("hole_peg") {
            viewModel.events
                .onEach {
                    when (it) {
                        HolePegEvent.Completed -> onComplete(viewModel.stateFlow.value)
                    }
                }
                .collect()
        }

        val state by viewModel.stateFlow.collectAsState()
        HolePeg(
            state = state,
            onDragStart = { viewModel.execute(StartDragging) },
            onDrag = { viewModel.execute(OnDrag(it)) },
            onDragEnd = { viewModel.execute(EndDragging(it)) }
        )

    }

}

@Composable
private fun HolePeg(
    state: HolePegState,
    onDragStart: () -> Unit = {},
    onDrag: (Float) -> Unit = {},
    onDragEnd: (Boolean) -> Unit = {}
) {

    val attempt = state.currentAttempt

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(state.step?.backgroundColor.toColor())
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        if (attempt != null) {
            StepHeader(
                title = getTitle(title = state.step?.title),
                description = getDescription(state.step, attempt.step.target, state.isDragging),
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            HoleAttemptPegProgress(
                attempt = attempt,
                color = state.step?.progressColor.toColor(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            )
            HolePegPoint(
                attempt = attempt,
                pointColor = state.step?.pointColor.toColor(),
                targetColor = state.step?.targetColor.toColor(),
                onDragStart = onDragStart,
                onDrag = onDrag,
                onDragEnd = onDragEnd
            )
        }
    }

}

@Composable
private fun getTitle(title: String?): String =
    title ?: stringResource(id = R.string.HOLE_PEG_title)

@Composable
private fun getDescription(
    step: HolePegStep?,
    targetPosition: HolePegTargetPosition,
    isDragging: Boolean
): String {

    val stepDescription =
        when (targetPosition) {
            HolePegTargetPosition.End ->
                step?.descriptionEnd
                    ?: stringResource(id = R.string.HOLE_PEG_description_end)
            HolePegTargetPosition.EndCenter ->
                step?.descriptionEndCenter
                    ?: stringResource(id = R.string.HOLE_PEG_description_end_center)
            HolePegTargetPosition.Start ->
                step?.descriptionStart
                    ?: stringResource(id = R.string.HOLE_PEG_description_start)
            HolePegTargetPosition.StartCenter ->
                step?.descriptionStartCenter
                    ?: stringResource(id = R.string.HOLE_PEG_description_start_center)
        }
    val draggingText =
        if (isDragging) step?.descriptionRelease ?: stringResource(id = R.string.HOLE_PEG_release)
        else step?.descriptionGrab ?: stringResource(id = R.string.HOLE_PEG_grab)

    return "$stepDescription\n$draggingText"

}

@Preview
@Composable
private fun HolePegPreview() {
    HolePeg(
        state =
        HolePegState(
            HolePegStep(
                identifier = "",
                block = Block("", Color.BLACK),
                backgroundColor = Color.WHITE,
                titleColor = Color.BLACK,
                descriptionColor = Color.BLACK,
                progressColor = Color.MAGENTA,
                pointColor = Color.MAGENTA,
                targetColor = Color.MAGENTA,
                subSteps =
                listOf(
                    HolePegSubStep(
                        HolePegPointPosition.Start,
                        HolePegTargetPosition.EndCenter
                    )
                )
            ),
            false
        )
    )
}