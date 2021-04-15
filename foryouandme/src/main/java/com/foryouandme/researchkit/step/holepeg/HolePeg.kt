package com.foryouandme.researchkit.step.holepeg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.R
import com.foryouandme.researchkit.step.common.StepHeader
import com.foryouandme.researchkit.step.holepeg.HolePegSateEvent.EndDragging
import com.foryouandme.researchkit.step.holepeg.HolePegSateEvent.StartDragging
import com.foryouandme.ui.compose.toColor

@Preview
@Composable
fun HolePeg(viewModel: HolePegViewModel = viewModel()) {

    val state by viewModel.stateFlow.collectAsState()
    val attempt = state.currentAttempt

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(state.step?.backgroundColor.toColor())
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        StepHeader(
            title = getTitle(title = state.step?.title),
            description = getDescription(state.step?.descriptionShape, state.isDragging),
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        )
        if (attempt != null) {
            Spacer(modifier = Modifier.height(10.dp))
            HoleAttemptPegProgress(
                attempt = attempt,
                color = state.step?.progressColor.toColor(),
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp)
            )
            HolePegPoint(
                attempt = attempt,
                onDragStart = { viewModel.execute(StartDragging) },
                onDragEnd = { viewModel.execute(EndDragging(it)) }
            )
        }
    }

}

@Composable
private fun getTitle(title: String?): String =
    title ?: stringResource(id = R.string.HOLE_PEG_title)

@Composable
private fun getDescription(description: String?, isDragging: Boolean): String {

    val stepDescription =
        description ?: stringResource(id = R.string.HOLE_PEG_description_shape)
    val draggingText =
        if (isDragging) stringResource(id = R.string.HOLE_PEG_release)
        else stringResource(id = R.string.HOLE_PEG_grab)

    return "$stepDescription\n$draggingText"

}