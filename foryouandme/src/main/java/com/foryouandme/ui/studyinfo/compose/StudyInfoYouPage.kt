package com.foryouandme.ui.studyinfo.compose

import android.content.pm.PackageInfo
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.toData
import com.foryouandme.core.ext.errorToast
import com.foryouandme.core.ext.toBase64ImageSource
import com.foryouandme.core.ext.toImageSource
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.compose.menu.MenuItem
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.studyinfo.StudyInfoAction
import com.foryouandme.ui.studyinfo.StudyInfoEvent
import com.foryouandme.ui.studyinfo.StudyInfoState
import com.foryouandme.ui.studyinfo.StudyInfoViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@Composable
fun StudyInfoPage(
    viewModel: StudyInfoViewModel,
    onAboutYouClicked: () -> Unit = {},
    onContactClicked: () -> Unit = {},
    onRewardsClicked: () -> Unit = {},
    onFAQClicked: () -> Unit = {},
) {

    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = "study_info") {
        viewModel.execute(StudyInfoAction.GetStudyInfo)
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.eventsFlow
            .unwrapEvent("study_info")
            .onEach {
                when(it) {
                    is StudyInfoEvent.StudyInfoError -> context.errorToast(it.error)
                }
            }
            .collect()
    }

    ForYouAndMeTheme(
        state.configuration,
        { viewModel.execute(StudyInfoAction.GetConfiguration) }
    ) {
        StudyInfoPage(
            state = state,
            configuration = it,
            imageConfiguration = viewModel.imageConfiguration,
            onAboutYouClicked = onAboutYouClicked,
            onContactClicked = onContactClicked,
            onRewardsClicked = onRewardsClicked,
            onFAQClicked = onFAQClicked,
        )
    }

}

@Composable
fun StudyInfoPage(
    state: StudyInfoState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onAboutYouClicked: () -> Unit = {},
    onContactClicked: () -> Unit = {},
    onRewardsClicked: () -> Unit = {},
    onFAQClicked: () -> Unit = {},
) {
    StatusBar(color = configuration.theme.primaryColorStart.value)

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(configuration.theme.secondaryColor.value)
    ) {
        StudyInfoHeader(
            configuration = configuration,
            imageConfiguration = imageConfiguration,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.45f)
                .background(configuration.theme.primaryColorStart.value),
        )
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            MenuItem(
                text = configuration.text.studyInfo.aboutYou,
                icon = imageConfiguration.aboutYou().toImageSource(),
                configuration = configuration,
                imageConfiguration = imageConfiguration,
                onClick = onAboutYouClicked
            )
            Divider(color = configuration.theme.fourthTextColor.value)
            when (state.studyInfo) {
                is LazyData.Data -> {
                    if (state.studyInfo.value.informationPage != null)
                        MenuItem(
                            text = state.studyInfo.value.informationPage.title,
                            icon =
                            state.studyInfo.value.informationPage.image?.toBase64ImageSource()
                                ?: imageConfiguration.contactInfo().toImageSource(),
                            configuration = configuration,
                            imageConfiguration = imageConfiguration,
                            onClick = onContactClicked
                        )
                    if (state.studyInfo.value.rewardPage != null)
                        MenuItem(
                            text = state.studyInfo.value.rewardPage.title,
                            icon =
                            state.studyInfo.value.rewardPage.image?.toBase64ImageSource()
                                ?: imageConfiguration.rewards().toImageSource(),
                            configuration = configuration,
                            imageConfiguration = imageConfiguration,
                            onClick = onRewardsClicked
                        )
                    if (state.studyInfo.value.faqPage != null)
                        MenuItem(
                            text = state.studyInfo.value.faqPage.title,
                            icon =
                            state.studyInfo.value.faqPage.image?.toBase64ImageSource()
                                ?: imageConfiguration.faq().toImageSource(),
                            configuration = configuration,
                            imageConfiguration = imageConfiguration,
                            onClick = onFAQClicked
                        )
                    Spacer(modifier = Modifier.height(30.dp))
                }
                is LazyData.Loading ->
                    Loading(
                        configuration = configuration,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                    )
                else -> Unit
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = getAppVersion(),
                style = MaterialTheme.typography.h3,
                color = configuration.theme.fourthTextColor.value,
                modifier =
                Modifier
                    .align(Alignment.Start)
                    .padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
@ReadOnlyComposable
private fun getAppVersion(): String {

    val packageInfo: PackageInfo =
        LocalContext.current
            .packageManager
            .getPackageInfo(
                LocalContext.current.packageName,
                0
            )

    val versionCode =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            packageInfo.longVersionCode
        else
            packageInfo.versionCode

    return "Version: ${packageInfo.versionName} (${versionCode})"

}

@Preview
@Composable
private fun StudyInfoPagePreview() {
    ForYouAndMeTheme(configuration = Configuration.mock().toData()) {
        StudyInfoPage(
            state = StudyInfoState.mock(),
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}