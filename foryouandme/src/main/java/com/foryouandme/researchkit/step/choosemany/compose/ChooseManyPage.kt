package com.foryouandme.researchkit.step.choosemany.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.RelocationRequester
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.getColor
import com.foryouandme.core.ext.getText
import com.foryouandme.researchkit.result.MultipleAnswerResult
import com.foryouandme.researchkit.skip.SkipTarget
import com.foryouandme.researchkit.step.choosemany.ChooseManyAction.*
import com.foryouandme.researchkit.step.choosemany.ChooseManyEvent
import com.foryouandme.researchkit.step.choosemany.ChooseManyState
import com.foryouandme.researchkit.step.choosemany.ChooseManyViewModel
import com.foryouandme.researchkit.step.compose.QuestionPage
import com.foryouandme.ui.compose.ForYouAndMeTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ChooseManyPage(
    viewModel: ChooseManyViewModel,
    onNext: (MultipleAnswerResult?) -> Unit,
    onSkip: (MultipleAnswerResult?, SkipTarget) -> Unit
) {

    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.eventsFlow
            .unwrapEvent("choose_many")
            .onEach {
                when (it) {
                    is ChooseManyEvent.Next -> onNext(it.result)
                    is ChooseManyEvent.Skip -> onSkip(it.result, it.target)
                }
            }
            .collect()
    }


    ForYouAndMeTheme {
        ChooseManyPage(
            state = state,
            onAnswerClicked = { viewModel.execute(Answer(it.answer.id)) },
            onTextChanged = { item, text ->
                viewModel.execute(AnswerTextChange(item.answer.id, text))
            },
            onNext = { viewModel.execute(Next) }
        )
    }

}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
private fun ChooseManyPage(
    state: ChooseManyState,
    onAnswerClicked: (ChooseManyAnswerData) -> Unit = {},
    onTextChanged: (ChooseManyAnswerData, String) -> Unit = { _, _ -> },
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
            state.answers.forEach { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    ChooseManyAnswerItem(
                        data = item,
                        onAnswerClicked = onAnswerClicked,
                        onTextChanged = onTextChanged,
                        onTextFocused = {}
                    )
                }
            }
        }
}