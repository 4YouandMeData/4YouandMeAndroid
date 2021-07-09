package com.foryouandme.ui.integration.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.ext.web.asIntegrationCookies
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.lazydata.LoadingError
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.verticalGradient
import com.foryouandme.ui.compose.web.Web
import com.foryouandme.ui.integration.IntegrationLoginAction.GetUser
import com.foryouandme.ui.integration.IntegrationLoginState
import com.foryouandme.ui.integration.IntegrationLoginViewModel

@Composable
fun IntegrationLoginPage(
    integrationLoginViewModel: IntegrationLoginViewModel = viewModel(),
    url: String,
    onBack: () -> Unit = {}
) {

    val state by integrationLoginViewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(configuration = state.configuration) {
        IntegrationLoginPage(
            state = state,
            configuration = it,
            imageConfiguration = integrationLoginViewModel.imageConfiguration,
            url = url,
            onBack = onBack,
            onRetryUser = { integrationLoginViewModel.execute(GetUser) }
        )
    }

}

@Composable
fun IntegrationLoginPage(
    state: IntegrationLoginState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    url: String,
    onBack: () -> Unit = {},
    onRetryUser: () -> Unit = {}
) {
    StatusBar(color = configuration.theme.primaryColorStart.value)
    LoadingError(
        data = state.user,
        configuration = configuration,
        onRetryClicked = onRetryUser
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .background(configuration.theme.secondaryColor.value)
        ) {
            ForYouAndMeTopAppBar(
                imageConfiguration = imageConfiguration,
                icon = TopAppBarIcon.Back,
                modifier =
                Modifier.background(configuration.theme.verticalGradient),
                onBack = onBack
            )
            Web(
                url = url,
                configuration = configuration,
                showProgress = true,
                success = onBack,
                failure = onBack,
                cookies = it.token.asIntegrationCookies(),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
fun IntegrationLoginPagePreview() {
    IntegrationLoginPage(
        state = IntegrationLoginState.mock(),
        configuration = Configuration.mock(),
        imageConfiguration = ImageConfiguration.mock(),
        url = "url"
    )
}