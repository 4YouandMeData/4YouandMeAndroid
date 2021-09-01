package com.foryouandme.researchkit.step.scale.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.getColor
import com.foryouandme.core.ext.getText
import com.foryouandme.researchkit.result.SingleIntAnswerResult
import com.foryouandme.researchkit.skip.SkipTarget
import com.foryouandme.researchkit.step.compose.QuestionPageLazy
import com.foryouandme.researchkit.step.scale.ScaleAction.*
import com.foryouandme.researchkit.step.scale.ScaleEvents
import com.foryouandme.researchkit.step.scale.ScaleState
import com.foryouandme.researchkit.step.scale.ScaleViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ScalePage(
    viewModel: ScaleViewModel,
    onNext: (SingleIntAnswerResult?) -> Unit,
    onSkip: (SingleIntAnswerResult?, SkipTarget) -> Unit
) {

    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.eventFlow
            .unwrapEvent("scale")
            .onEach {
                when (it) {
                    is ScaleEvents.Next -> onNext(it.result)
                    is ScaleEvents.Skip -> onSkip(it.result, it.target)
                }
            }
            .collect()
    }


    ForYouAndMeTheme {
        ScalePage(
            state = state,
            onValueChange = { viewModel.execute(SelectValue(it)) },
            onValueChangeFinished = { viewModel.execute(EndValueSelection) },
            onNext = { viewModel.execute(Next) }
        )
    }

}

@Composable
private fun ScalePage(
    state: ScaleState,
    onValueChange: (Float) -> Unit = {},
    onValueChangeFinished: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    if (state.step != null)
        QuestionPageLazy(
            backgroundColor = state.step.backgroundColor.getColor(),
            question = state.step.question.getText(),
            questionColor = state.step.questionColor.getColor(),
            buttonImage = state.step.buttonImage,
            isNextEnabled = state.canGoNext,
            onNext = onNext
        ) {
            item {
                Text(
                    text = state.value.toString(),
                    style = MaterialTheme.typography.h1,
                    color = state.step.valueColor.getColor()
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Slider(
                    value = state.valuePercent,
                    steps = maxOf(
                        state.step.maxValue - state.step.minValue - 1,
                        0
                    ).div(state.step.interval),
                    onValueChange = onValueChange,
                    onValueChangeFinished = onValueChangeFinished,
                    colors =
                    SliderDefaults.colors(
                        thumbColor = state.step.progressColor.getColor(),
                        activeTrackColor = state.step.progressColor.getColor(),
                        activeTickColor = state.step.progressColor.getColor()
                    )
                )
            }
        }
}