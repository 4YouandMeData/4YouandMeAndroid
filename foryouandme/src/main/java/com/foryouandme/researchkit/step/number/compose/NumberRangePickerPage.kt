package com.foryouandme.researchkit.step.number.compose

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.getColor
import com.foryouandme.core.ext.getText
import com.foryouandme.researchkit.result.SingleStringAnswerResult
import com.foryouandme.researchkit.skip.SkipTarget
import com.foryouandme.researchkit.step.compose.QuestionPage
import com.foryouandme.researchkit.step.number.NumberPickerAction.Next
import com.foryouandme.researchkit.step.number.NumberPickerAction.SelectValue
import com.foryouandme.researchkit.step.number.NumberPickerEvents
import com.foryouandme.researchkit.step.number.NumberPickerState
import com.foryouandme.researchkit.step.number.NumberRangePickerViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
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
                when (it) {
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
        QuestionPage(
            backgroundColor = state.step.backgroundColor.getColor(),
            question = state.step.question.getText(),
            questionColor = state.step.questionColor.getColor(),
            buttonImage = state.step.buttonImage,
            isNextEnabled = state.canGoNext,
            onNext = onNext
        ) {
            NumberPicker(
                value = state.selectedIndex,
                numbersColumnHeight = 50.dp,
                values = state.values,
                arrowColor = state.step.arrowColor.getColor(),
                onValueChange = onValueSelected,
                modifier =
                Modifier
                    .width(100.dp)
                    .height(110.dp),
            )
        }
}
