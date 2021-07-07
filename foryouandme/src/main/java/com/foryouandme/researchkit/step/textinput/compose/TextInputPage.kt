package com.foryouandme.researchkit.step.textinput.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.getColor
import com.foryouandme.core.ext.getText
import com.foryouandme.researchkit.result.SingleStringAnswerResult
import com.foryouandme.researchkit.step.compose.QuestionPage
import com.foryouandme.researchkit.step.textinput.TextInputAction.Next
import com.foryouandme.researchkit.step.textinput.TextInputAction.SetText
import com.foryouandme.researchkit.step.textinput.TextInputEvent
import com.foryouandme.researchkit.step.textinput.TextInputState
import com.foryouandme.researchkit.step.textinput.TextInputViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.textfield.EntryText
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun TextInputPage(
    viewModel: TextInputViewModel,
    onNext: (SingleStringAnswerResult?) -> Unit,
) {

    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.eventsFlow
            .unwrapEvent("range")
            .onEach {
                when (it) {
                    is TextInputEvent.Next -> onNext(it.result)
                }
            }
            .collect()
    }


    ForYouAndMeTheme {
        TextInputPage(
            state = state,
            onValueChange = { viewModel.execute(SetText(it)) },
            onNext = { viewModel.execute(Next) }
        )
    }

}

@Composable
private fun TextInputPage(
    state: TextInputState,
    onValueChange: (String) -> Unit = {},
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
                EntryText(
                    text = state.text,
                    labelColor = state.step.placeholderColor.getColor(),
                    placeholderColor = state.step.placeholderColor.getColor(),
                    textColor = state.step.textColor.getColor(),
                    indicatorColor = state.step.placeholderColor.getColor(),
                    cursorColor = state.step.textColor.getColor(),
                    label = state.step.placeholder?.getText(),
                    maxCharacter = state.step.maxCharacters,
                    modifier = Modifier.fillMaxWidth(),
                    onTextChanged = onValueChange
                )
            }
        }
}