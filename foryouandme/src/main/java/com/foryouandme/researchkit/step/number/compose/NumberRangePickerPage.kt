package com.foryouandme.researchkit.step.number.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.getText
import com.foryouandme.researchkit.result.SingleStringAnswerResult
import com.foryouandme.researchkit.skip.SkipTarget
import com.foryouandme.researchkit.step.compose.StepFooter
import com.foryouandme.researchkit.step.number.NumberPickerAction
import com.foryouandme.researchkit.step.number.NumberPickerAction.*
import com.foryouandme.researchkit.step.number.NumberPickerEvents
import com.foryouandme.researchkit.step.number.NumberPickerState
import com.foryouandme.researchkit.step.number.NumberRangePickerViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.toColor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun NumberRangePickerPage(
    numberRangePickerViewModel: NumberRangePickerViewModel = viewModel(),
    onNext: (SingleStringAnswerResult?) -> Unit,
    onSkip: (SingleStringAnswerResult?, SkipTarget) -> Unit
) {

    val state by numberRangePickerViewModel.stateFlow.collectAsState()

    LaunchedEffect(numberRangePickerViewModel) {
        numberRangePickerViewModel.eventFlow
            .unwrapEvent("number_range_picker")
            .onEach {
                when(it) {
                    is NumberPickerEvents.Next -> onNext(it.result)
                    is NumberPickerEvents.Skip -> onSkip(it.result, it.target)
                }
            }
            .collect()
    }

    ForYouAndMeTheme {
        NumberRangePickerPage(
            state = state,
            onValueSelected = { numberRangePickerViewModel.execute(SelectValue(it)) },
            onNext = { numberRangePickerViewModel.execute(Next) }
        )
    }

}

@Composable
fun NumberRangePickerPage(
    state: NumberPickerState,
    onValueSelected: (Int) -> Unit = {},
    onNext: () -> Unit = {}
) {
    if (state.step != null)
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .background(state.step.backgroundColor.toColor())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = state.step.question.getText(),
                        style = MaterialTheme.typography.h1,
                        color = state.step.questionColor.toColor(),
                        textAlign = TextAlign.Center,
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    NumberPicker(
                        value = state.selectedIndex,
                        numbersColumnHeight = 50.dp,
                        values = state.values,
                        arrowColor = state.step.arrowColor.toColor(),
                        onValueChange = onValueSelected,
                        modifier =
                        Modifier
                            .width(100.dp)
                            .height(110.dp),
                    )
                }
            }
            StepFooter(
                color = state.step.backgroundColor.toColor(),
                button = state.step.buttonImage,
                shadowColor = state.step.shadowColor.toColor(),
                onClick = onNext,
                isEnabled = state.canGoNext
            )
        }
}