package com.foryouandme.ui.auth.signup.later.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.auth.signup.later.SignUpLaterAction.GetConfiguration
import com.foryouandme.ui.auth.signup.later.SignUpLaterViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon

@Composable
fun SignUpLaterPage(
    viewModel: SignUpLaterViewModel,
    onBack: () -> Unit = {},
) {

    val state by viewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { viewModel.execute(GetConfiguration) }
    ) {
        SignUpLaterPage(
            configuration = it,
            imageConfiguration = viewModel.imageConfiguration,
            onBack = onBack,
        )
    }

}

@Composable
private fun SignUpLaterPage(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onBack: () -> Unit = {},
) {
    StatusBar(color = configuration.theme.primaryColorStart.value)
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(configuration.theme.primaryColorStart.value)
    ) {
        ForYouAndMeTopAppBar(
            imageConfiguration = imageConfiguration,
            icon = TopAppBarIcon.Back,
            modifier = Modifier.fillMaxWidth(),
            onBack = onBack
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 25.dp, vertical = 20.dp)
        ) {

            Image(
                painterResource(id = imageConfiguration.logo()),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = configuration.text.signUpLater.body,
                color = configuration.theme.secondaryColor.value,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

        }

        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = configuration.theme.primaryColorEnd.value
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier =
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(horizontal = 20.dp)
        ) {
            ForYouAndMeButton(
                text = configuration.text.signUpLater.confirmButton,
                backgroundColor = configuration.theme.secondaryColor.value,
                textColor = configuration.theme.primaryColorEnd.value,
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Preview
@Composable
fun SignUpLaterPreview() {
    ForYouAndMeTheme {
        SignUpLaterPage(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}