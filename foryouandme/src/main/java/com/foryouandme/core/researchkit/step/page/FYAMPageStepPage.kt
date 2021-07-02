package com.foryouandme.core.researchkit.step.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.page.Page
import com.foryouandme.ui.compose.statusbar.StatusBar

@Composable
fun FYAMPageStepPage(
    viewModel: FYAMPageStepViewModel,
    next: () -> Unit = {},
    reschedule: () -> Unit = {}
) {

    val state by viewModel.stateFlow.collectAsState()

    ForYouAndMeTheme {
        FYAMPageStepPage(
            state = state,
            imageConfiguration = viewModel.imageConfiguration,
            next = next,
            reschedule = reschedule
        )
    }

}

@Composable
private fun FYAMPageStepPage(
    state: FYAMPageStepState,
    imageConfiguration: ImageConfiguration,
    next: () -> Unit = {},
    reschedule: () -> Unit = {}
) {
    if (state.step != null) {
        StatusBar(color = state.step.configuration.theme.secondaryColor.value)

        val page =
            if (state.step.remind)
                state.step.page.copy(
                    specialLinkLabel = state.step.configuration.text.task.remindButton,
                    specialLinkValue = "remind"
                )
            else
                state.step.page

        val specialAction: ((String) -> Unit)? =
            if (state.step.remind) {
                { reschedule() }
            } else null

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(state.step.configuration.theme.secondaryColor.value)
                .padding(top = 55.dp)
        ) {
            Page(
                configuration = state.step.configuration,
                imageConfiguration = imageConfiguration,
                page = page,
                pageType = state.step.pageType,
                action1 = { next() },
                action2 = null,
                extraStringAction = null,
                extraPageAction = null,
                specialStringAction = specialAction
            )
        }
    }
}