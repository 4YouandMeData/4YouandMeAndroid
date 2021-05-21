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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.menu.MenuItem
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.studyinfo.StudyInfoAction
import com.foryouandme.ui.studyinfo.StudyInfoState
import com.foryouandme.ui.studyinfo.StudyInfoViewModel


@Composable
fun StudyInfoPage(
    studyInfoViewModel: StudyInfoViewModel = viewModel(),
    onAboutYouClicked: () -> Unit = {},
    onContactClicked: () -> Unit = {},
    onRewardsClicked: () -> Unit = {},
    onFAQClicked: () -> Unit = {},
) {

    val state by studyInfoViewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(
        state.configuration,
        { studyInfoViewModel.execute(StudyInfoAction.GetConfiguration) }
    ) {
        StudyInfoPage(
            state = state,
            configuration = it,
            imageConfiguration = studyInfoViewModel.imageConfiguration,
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
                icon = imageConfiguration.aboutYou(),
                configuration = configuration,
                imageConfiguration = imageConfiguration,
                onClick = onAboutYouClicked
            )
            Divider(color = configuration.theme.fourthTextColor.value)
            MenuItem(
                text = configuration.text.studyInfo.contactInfo,
                icon = imageConfiguration.contactInfo(),
                configuration = configuration,
                imageConfiguration = imageConfiguration,
                onClick = onContactClicked
            )
            MenuItem(
                text = configuration.text.studyInfo.rewards,
                icon = imageConfiguration.rewards(),
                configuration = configuration,
                imageConfiguration = imageConfiguration,
                onClick = onRewardsClicked
            )
            MenuItem(
                text = configuration.text.studyInfo.faq,
                icon = imageConfiguration.faq(),
                configuration = configuration,
                imageConfiguration = imageConfiguration,
                onClick = onFAQClicked
            )
            Spacer(modifier = Modifier.height(30.dp))
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