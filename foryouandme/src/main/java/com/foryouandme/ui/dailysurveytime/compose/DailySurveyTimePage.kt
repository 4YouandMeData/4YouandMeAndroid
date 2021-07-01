package com.foryouandme.ui.dailysurveytime.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.errorToast
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.lazydata.LoadingError
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.verticalGradient
import com.foryouandme.ui.dailysurveytime.DailySurveyTimeAction.*
import com.foryouandme.ui.dailysurveytime.DailySurveyTimeEvent
import com.foryouandme.ui.dailysurveytime.DailySurveyTimeState
import com.foryouandme.ui.dailysurveytime.DailySurveyTimeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.LocalTime

@ExperimentalAnimationApi
@Composable
fun DailySurveyTimePage(
    viewModel: DailySurveyTimeViewModel,
    onBack: () -> Unit
) {

    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel) {
        viewModel.eventsFlow
            .unwrapEvent("daily_survey_time")
            .onEach {
                when (it) {
                    is DailySurveyTimeEvent.SaveError ->
                        context.errorToast(it.error, state.configuration.dataOrNull())
                    DailySurveyTimeEvent.Saved ->
                        onBack()
                }
            }
            .collect()
    }

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { viewModel.execute(GetConfiguration) }
    ) { configuration ->
        DailySurveyTimePage(
            state = state,
            configuration = configuration,
            imageConfiguration = viewModel.imageConfiguration,
            onBack = onBack,
            onUserSettingsRetry = { viewModel.execute(GetUserSettings) },
            onTimeSelected = { viewModel.execute(UpdateTime(it)) },
            onSaveClicked = { viewModel.execute(SaveUserSettings) }
        )
    }

}

@ExperimentalAnimationApi
@Composable
private fun DailySurveyTimePage(
    state: DailySurveyTimeState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onBack: () -> Unit = {},
    onUserSettingsRetry: () -> Unit = {},
    onTimeSelected: (LocalTime) -> Unit = {},
    onSaveClicked: () -> Unit = {}
) {
    StatusBar(color = configuration.theme.primaryColorStart.value)
    LoadingError(
        data = state.userSettings,
        configuration = configuration,
        onRetryClicked = onUserSettingsRetry,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(configuration.theme.secondaryColor.value)
            ) {
                ForYouAndMeTopAppBar(
                    imageConfiguration = imageConfiguration,
                    icon = TopAppBarIcon.Back,
                    title = configuration.text.profile.dailySurveyTime.title,
                    titleColor = configuration.theme.secondaryColor.value,
                    modifier =
                    Modifier
                        .height(110.dp)
                        .background(configuration.theme.verticalGradient),
                    onBack = onBack
                )
                Spacer(modifier = Modifier.height(30.dp))
                EntryTime(
                    time = it.dailySurveyTime,
                    configuration = configuration,
                    imageConfiguration = imageConfiguration,
                    onTimeSelected = onTimeSelected,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                ForYouAndMeButton(
                    text = configuration.text.profile.dailySurveyTime.save,
                    backgroundColor = configuration.theme.primaryColorEnd.value,
                    textColor = configuration.theme.secondaryColor.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    onClick = onSaveClicked
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        AnimatedVisibility(
            visible = state.save.isLoading(),
            enter = fadeIn(),
            exit = fadeOut()
        ) { Loading(configuration = configuration, Modifier.fillMaxSize())}
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
private fun DailySurveyTimePagePreview() {
    ForYouAndMeTheme {
        DailySurveyTimePage(
            state = DailySurveyTimeState.mock(),
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock(),
        )
    }
}