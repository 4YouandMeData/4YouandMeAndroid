package com.foryouandme.ui.auth.signup.info.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.auth.signup.info.SignUpInfoAction.GetConfiguration
import com.foryouandme.ui.auth.signup.info.SignUpInfoToEnterPhone
import com.foryouandme.ui.auth.signup.info.SignUpInfoToPinCode
import com.foryouandme.ui.auth.signup.info.SignUpInfoViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon

@Composable
fun SignUpInfoPage(
    viewModel: SignUpInfoViewModel,
    onBack: () -> Unit = {},
    signIn: (NavigationAction) -> Unit = {},
    signUpLater: () -> Unit = {}
) {

    val state by viewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { viewModel.execute(GetConfiguration) }
    ) {
        SignUpInfoPage(
            configuration = it,
            imageConfiguration = viewModel.imageConfiguration,
            onBack = onBack,
            signIn = {
                signIn(
                    if (it.pinCodeLogin) SignUpInfoToPinCode
                    else SignUpInfoToEnterPhone
                )
            },
            signUpLater = signUpLater
        )
    }

}

@Composable
private fun SignUpInfoPage(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onBack: () -> Unit = {},
    signIn: () -> Unit = {},
    signUpLater: () -> Unit = {}
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
                text = configuration.text.intro.title,
                color = configuration.theme.secondaryColor.value,
                style = MaterialTheme.typography.h1,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = configuration.text.intro.body,
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

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .clickable { signIn() }
        ) {
            Image(
                painter = painterResource(id = imageConfiguration.nextStep()),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = configuration.text.intro.login,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = configuration.theme.secondaryColor.value,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .clickable { signUpLater() }
        ) {
            Image(
                painter = painterResource(id = imageConfiguration.nextStep()),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = configuration.text.intro.back,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = configuration.theme.secondaryColor.value,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Preview
@Composable
fun SignUpInfoPreview() {
    ForYouAndMeTheme {
        SignUpInfoPage(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}