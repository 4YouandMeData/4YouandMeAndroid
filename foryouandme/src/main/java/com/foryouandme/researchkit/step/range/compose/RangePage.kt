package com.foryouandme.researchkit.step.range.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.foryouandme.researchkit.step.compose.QuestionPage
import com.foryouandme.researchkit.step.range.RangeAction
import com.foryouandme.researchkit.step.range.RangeAction.*
import com.foryouandme.researchkit.step.range.RangeEvents
import com.foryouandme.researchkit.step.range.RangeState
import com.foryouandme.researchkit.step.range.RangeViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.lang.Integer.max

@Composable
fun RangePage(
    viewModel: RangeViewModel,
    onNext: (SingleIntAnswerResult?) -> Unit,
    onSkip: (SingleIntAnswerResult?, SkipTarget) -> Unit
) {

    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.eventFlow
            .unwrapEvent("range")
            .onEach {
                when (it) {
                    is RangeEvents.Next -> onNext(it.result)
                    is RangeEvents.Skip -> onSkip(it.result, it.target)
                }
            }
            .collect()
    }


    ForYouAndMeTheme {
        RangePage(
            state = state,
            onValueChange = { viewModel.execute(SelectValue(it)) },
            onValueChangeFinished = { viewModel.execute(EndValueSelection) },
            onNext = { viewModel.execute(Next) }
        )
    }

}

@Composable
private fun RangePage(
    state: RangeState,
    onValueChange: (Float) -> Unit = {},
    onValueChangeFinished: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    if (state.step != null)
        QuestionPage(
            backgroundColor = state.step.backgroundColor.getColor(),
            question = state.step.question.getText(),
            questionColor = state.step.questionColor.getColor(),
            buttonImage = state.step.buttonImage,
            shadowColor = state.step.shadowColor.getColor(),
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
                    steps = maxOf(state.step.maxValue - state.step.minValue - 1, 0),
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
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (state.step.minDisplayValue != null)
                        Text(
                            text = state.step.minDisplayValue.getText(),
                            style = MaterialTheme.typography.h3,
                            color = state.step.minDisplayColor.getColor()
                        )
                    Spacer(modifier = Modifier.weight(1f))
                    if (state.step.maxDisplayValue != null)
                        Text(
                            text = state.step.maxDisplayValue.getText(),
                            style = MaterialTheme.typography.h3,
                            color = state.step.maxDisplayColor.getColor()
                        )
                }
            }
        }
}