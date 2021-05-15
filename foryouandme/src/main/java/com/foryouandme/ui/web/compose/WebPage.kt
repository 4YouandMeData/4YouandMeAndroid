package com.foryouandme.ui.web.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.web.Web
import com.foryouandme.ui.web.WebViewModel

@Composable
fun WebPage(
    webViewModel: WebViewModel = viewModel(),
    url: String,
    onBack: () -> Unit
) {

    val state by webViewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(configuration = state.configuration) {
        WebPage(
            configuration = it,
            imageConfiguration = webViewModel.imageConfiguration,
            url = url,
            onBack = onBack
        )
    }

}

@Composable
fun WebPage(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    url: String,
    onBack: () -> Unit = {}
) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(configuration.theme.secondaryColor.value)
    ) {
        ForYouAndMeTopAppBar(
            imageConfiguration = imageConfiguration,
            icon = TopAppBarIcon.Close,
            modifier = Modifier.fillMaxWidth(),
            onBack = onBack
        )
        Web(
            url = url,
            configuration = configuration,
            showProgress = true,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun WebPage() {
    ForYouAndMeTheme {
        WebPage(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock(),
            url = "url",
        )
    }
}