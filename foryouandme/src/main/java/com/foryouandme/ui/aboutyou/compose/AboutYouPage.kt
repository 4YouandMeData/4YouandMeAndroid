package com.foryouandme.ui.aboutyou.compose

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User
import com.foryouandme.ui.aboutyou.AboutYouAction.GetConfiguration
import com.foryouandme.ui.aboutyou.AboutYouAction.GetUser
import com.foryouandme.ui.aboutyou.AboutYouState
import com.foryouandme.ui.aboutyou.AboutYouViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.lazydata.LoadingError
import com.foryouandme.ui.compose.menu.MenuItem
import com.foryouandme.ui.compose.statusbar.StatusBar
import org.threeten.bp.ZoneId

@Composable
fun AboutYouPage(
    aboutYouViewModel: AboutYouViewModel = viewModel(),
    onBack: () -> Unit = {},
    onPregnancyClicked: () -> Unit = {},
    onDevicesClicked: () -> Unit = {},
    onReviewConsentClicked: () -> Unit = {},
    onPermissionsClicked: () -> Unit = {},
    onDailySurveyTimeClicked: () -> Unit = {}
) {

    val state by aboutYouViewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(
        state.configuration,
        { aboutYouViewModel.execute(GetConfiguration) }
    ) {
        AboutYouPage(
            state = state,
            configuration = it,
            imageConfiguration = aboutYouViewModel.imageConfiguration,
            onUserError = { aboutYouViewModel.execute(GetUser) },
            onBack = onBack,
            onPregnancyClicked = onPregnancyClicked,
            onDevicesClicked = onDevicesClicked,
            onReviewConsentClicked = onReviewConsentClicked,
            onPermissionsClicked = onPermissionsClicked,
            onDailySurveyTimeClicked = onDailySurveyTimeClicked
        )
    }

}

@Composable
fun AboutYouPage(
    state: AboutYouState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onUserError: () -> Unit = {},
    onBack: () -> Unit = {},
    onPregnancyClicked: () -> Unit = {},
    onDevicesClicked: () -> Unit = {},
    onReviewConsentClicked: () -> Unit = {},
    onPermissionsClicked: () -> Unit = {},
    onDailySurveyTimeClicked: () -> Unit = {}
) {
    StatusBar(color = configuration.theme.primaryColorStart.value) {
        LoadingError(
            data = state.user,
            configuration = configuration,
            onRetryClicked = onUserError
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(configuration.theme.secondaryColor.value)
            ) {
                AboutYouHeader(
                    configuration = configuration,
                    imageConfiguration = imageConfiguration,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.35f)
                        .background(configuration.theme.primaryColorStart.value),
                    onBack = onBack
                )
                Column(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    if (it.customData.isNotEmpty())
                        MenuItem(
                            text = configuration.text.profile.firstItem,
                            icon = imageConfiguration.pregnancy(),
                            configuration = configuration,
                            imageConfiguration = imageConfiguration,
                            onClick = onPregnancyClicked
                        )
                    MenuItem(
                        text = configuration.text.profile.secondItem,
                        icon = imageConfiguration.devices(),
                        configuration = configuration,
                        imageConfiguration = imageConfiguration,
                        onClick = onDevicesClicked
                    )
                    Box(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        Spacer(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(configuration.theme.fourthTextColor.value)
                        )
                    }
                    MenuItem(
                        text = configuration.text.profile.thirdItem,
                        icon = imageConfiguration.reviewConsent(),
                        configuration = configuration,
                        imageConfiguration = imageConfiguration,
                        onClick = onReviewConsentClicked
                    )
                    MenuItem(
                        text = configuration.text.profile.fourthItem,
                        icon = imageConfiguration.permissions(),
                        configuration = configuration,
                        imageConfiguration = imageConfiguration,
                        onClick = onPermissionsClicked
                    )
                    if (configuration.text.profile.dailySurveyTimingHidden == 0)
                        MenuItem(
                            text = configuration.text.profile.fifthItem,
                            icon = imageConfiguration.dailySurveyTime(),
                            configuration = configuration,
                            imageConfiguration = imageConfiguration,
                            onClick = onDailySurveyTimeClicked
                        )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = configuration.text.profile.disclaimer,
                        style = MaterialTheme.typography.h3,
                        color = configuration.theme.fourthTextColor.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun AboutYouPagePreview() {
    ForYouAndMeTheme(configuration = Configuration.mock().toData()) {
        AboutYouPage(
            state =
            AboutYouState(
                user =
                User(
                    id = "",
                    email = "",
                    phoneNumber = "",
                    daysInStudy = 0,
                    identities = emptyList(),
                    onBoardingCompleted = true,
                    token = "",
                    customData = emptyList(),
                    timeZone = ZoneId.of("UTC"),
                    points = 0
                ).toData(),
                configuration =
                Configuration.mock().toData()
            ),
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}