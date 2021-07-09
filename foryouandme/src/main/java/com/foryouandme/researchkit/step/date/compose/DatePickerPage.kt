package com.foryouandme.researchkit.step.date.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.getColor
import com.foryouandme.core.ext.getText
import com.foryouandme.researchkit.result.SingleStringAnswerResult
import com.foryouandme.researchkit.step.compose.QuestionPage
import com.foryouandme.researchkit.step.date.DatePickerAction.Next
import com.foryouandme.researchkit.step.date.DatePickerAction.SetDate
import com.foryouandme.researchkit.step.date.DatePickerEvent
import com.foryouandme.researchkit.step.date.DatePickerState
import com.foryouandme.researchkit.step.date.DatePickerViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.textfield.EntryDate
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.LocalDate

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun DatePickerPage(
    viewModel: DatePickerViewModel,
    onNext: (SingleStringAnswerResult?) -> Unit,
) {

    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.eventsFlow
            .unwrapEvent("date_picker")
            .onEach {
                when (it) {
                    is DatePickerEvent.Next -> onNext(it.result)
                }
            }
            .collect()
    }


    ForYouAndMeTheme {
        DatePickerPage(
            state = state,
            onValueChange = { viewModel.execute(SetDate(it)) },
            onNext = { viewModel.execute(Next) }
        )
    }

}

@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
private fun DatePickerPage(
    state: DatePickerState,
    onValueChange: (LocalDate) -> Unit = {},
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
            image = state.step.image,
            onNext = onNext
        ) {
            item {
                EntryDate(
                    value = state.value,
                    entryDateColors = state.step.entryDateColors,
                    isEditable = true,
                    minDate = state.step.minDate,
                    maxDate = state.step.maxDate,
                    onDateSelected = onValueChange
                )
            }
        }
}