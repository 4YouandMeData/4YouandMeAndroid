package com.foryouandme.researchkit.step.chooseone.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.getColor
import com.foryouandme.core.ext.getText
import com.foryouandme.researchkit.result.SingleAnswerResult
import com.foryouandme.researchkit.skip.SkipTarget
import com.foryouandme.researchkit.step.chooseone.ChooseOneAction.*
import com.foryouandme.researchkit.step.chooseone.ChooseOneEvent
import com.foryouandme.researchkit.step.chooseone.ChooseOneState
import com.foryouandme.researchkit.step.chooseone.ChooseOneViewModel
import com.foryouandme.researchkit.step.compose.QuestionPage
import com.foryouandme.ui.compose.ForYouAndMeTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@ExperimentalAnimationApi
@Composable
fun ChooseOnePage(
    viewModel: ChooseOneViewModel,
    onNext: (SingleAnswerResult?) -> Unit,
    onSkip: (SingleAnswerResult?, SkipTarget) -> Unit
) {

    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.eventsFlow
            .unwrapEvent("choose_one")
            .onEach {
                when (it) {
                    is ChooseOneEvent.Next -> onNext(it.result)
                    is ChooseOneEvent.Skip -> onSkip(it.result, it.target)
                }
            }
            .collect()
    }


    ForYouAndMeTheme {
        ChooseOnePage(
            state = state,
            onAnswerClicked = { viewModel.execute(Answer(it.answer.id)) },
            onTextChanged = { item, text ->
                viewModel.execute(AnswerTextChange(item.answer.id, text))
            },
            onNext = { viewModel.execute(Next) }
        )
    }

}

@ExperimentalAnimationApi
@Composable
private fun ChooseOnePage(
    state: ChooseOneState,
    onAnswerClicked: (ChooseOneAnswerData) -> Unit = {},
    onTextChanged: (ChooseOneAnswerData, String) -> Unit = { _, _ -> },
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
            items(state.answers) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    ChooseOneAnswerItem(
                        data = it,
                        onAnswerClicked = onAnswerClicked,
                        onTextChanged = onTextChanged
                    )
                }
            }
        }
}