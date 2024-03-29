package com.foryouandme.researchkit.step.introduction.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.ext.getText
import com.foryouandme.entity.mock.Mock
import com.foryouandme.entity.source.TextSource
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.introduction.IntroductionState
import com.foryouandme.researchkit.step.introduction.IntroductionStep
import com.foryouandme.researchkit.step.introduction.IntroductionViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.toColor
import com.foryouandme.R
import com.foryouandme.researchkit.step.Block

@Composable
fun Introduction(introductionViewModel: IntroductionViewModel = viewModel(), onNext: () -> Unit) {

    val state by introductionViewModel.stateFlow.collectAsState()
    ForYouAndMeTheme {
        Introduction(state = state, onNext)
    }

}

@Composable
private fun Introduction(state: IntroductionState, onNext: () -> Unit = {}) {

    if (state.step != null) {

        BoxWithConstraints(
            modifier =
            Modifier.fillMaxSize()
        ) {

            val screenHeight = maxHeight

            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(state.step.backgroundColor.toColor())
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.4f)
                        .background(Color(0xffE3E3E3))
                        .padding(50.dp)
                ) {
                    val image = state.step.image.getOrNull(state.currentImageIndex)
                    if (image != null) {
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = "",
                            contentScale = ContentScale.Inside,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Column(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp, top = 20.dp, bottom = 25.dp)
                ) {
                    Text(
                        text = state.step.title.getText(),
                        style = MaterialTheme.typography.h1,
                        color = state.step.titleColor.toColor(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = state.step.description.getText(),
                        style = MaterialTheme.typography.body1,
                        color = state.step.descriptionColor.toColor(),
                        modifier = Modifier.fillMaxSize()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    ForYouAndMeButton(
                        text = state.step.button.getText(),
                        backgroundColor = state.step.buttonColor.toColor(),
                        textColor = state.step.buttonTextColor.toColor(),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onNext() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun IntroductionPreview() {
    ForYouAndMeTheme {
        Introduction(
            IntroductionState(
                IntroductionStep(
                    identifier = "id",
                    block = Block("", android.graphics.Color.BLACK),
                    back = Back(R.drawable.placeholder),
                    backgroundColor = android.graphics.Color.WHITE,
                    title = TextSource.Text(Mock.title),
                    titleColor = android.graphics.Color.BLACK,
                    description = TextSource.Text(Mock.body),
                    descriptionColor = android.graphics.Color.BLACK,
                    image = listOf(0),
                    button = TextSource.Text(Mock.button),
                    buttonColor = android.graphics.Color.WHITE,
                    buttonTextColor = android.graphics.Color.WHITE,
                )
            )
        )
    }
}