package com.foryouandme.ui.studyinfo.detail.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.lazydata.LoadingError
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.web.Web
import com.foryouandme.ui.studyinfo.detail.EStudyInfoType.*
import com.foryouandme.ui.studyinfo.detail.StudyInfoDetailAction.GetConfiguration
import com.foryouandme.ui.studyinfo.detail.StudyInfoDetailAction.GetStudyInfo
import com.foryouandme.ui.studyinfo.detail.StudyInfoDetailState
import com.foryouandme.ui.studyinfo.detail.StudyInfoDetailViewModel

@Composable
fun StudyInfoDetailPage(
    viewModel: StudyInfoDetailViewModel,
    onBack: () -> Unit
) {

    val state by viewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { viewModel.execute(GetConfiguration) }
    ) {
        StudyInfoDetailPage(
            state = state,
            configuration = it,
            imageConfiguration = viewModel.imageConfiguration,
            onStudyInfoRetry = { viewModel.execute(GetStudyInfo) },
            onBack = onBack
        )
    }

}

@Composable
private fun StudyInfoDetailPage(
    state: StudyInfoDetailState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onStudyInfoRetry: () -> Unit = {},
    onBack: () -> Unit = {}
) {

    StatusBar(color = configuration.theme.primaryColorStart.value)

    LoadingError(
        data = state.studyInfo,
        configuration = configuration,
        onRetryClicked = onStudyInfoRetry
    ) {

        val page =
            when (state.type) {
                INFO -> it.informationPage
                REWARD -> it.rewardPage
                FAQ -> it.faqPage
            }

        if (page != null) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(configuration.theme.secondaryColor.value)
            ) {
                ForYouAndMeTopAppBar(
                    imageConfiguration = imageConfiguration,
                    title = page.title,
                    titleColor = configuration.theme.secondaryColor.value,
                    icon = TopAppBarIcon.Back,
                    onBack = onBack,
                    modifier =
                    Modifier
                        .height(110.dp)
                        .background(configuration.theme.primaryColorStart.value)
                )
                Web(
                    html = page.body,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}