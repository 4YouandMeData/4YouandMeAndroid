package com.foryouandme.ui.auth.welcome.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.auth.welcome.WelcomeAction
import com.foryouandme.ui.auth.welcome.WelcomeViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.verticalGradient

@Composable
fun WelcomePage(viewModel: WelcomeViewModel, onStartClicked: () -> Unit) {

    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(key1 = "welcome") {
        viewModel.execute(WelcomeAction.ScreenViewed)
    }

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { viewModel.execute(WelcomeAction.GetConfiguration) }
    ) {
        WelcomePage(
            configuration = it,
            imageConfiguration = viewModel.imageConfiguration,
            onStartClicked = onStartClicked
        )
    }

}

@Composable
private fun WelcomePage(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onStartClicked: () -> Unit = {}
) {

    val alpha = remember { Animatable(0f) }
    LaunchedEffect(key1 = "welcome") {
        alpha.animateTo(1f, tween(800, 500))
    }

    StatusBar(color = configuration.theme.primaryColorStart.value)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(configuration.theme.verticalGradient)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = imageConfiguration.logoStudy()),
                    contentDescription = null,
                    modifier =
                    Modifier
                        .width(100.dp)
                        .aspectRatio(getImageRatio(id = imageConfiguration.logoStudy()))
                )
                Spacer(modifier = Modifier.height(50.dp))
            }

            Image(
                painter = painterResource(id = imageConfiguration.logoStudySecondary()),
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .aspectRatio(getImageRatio(id = imageConfiguration.logoStudySecondary()))
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier =
            Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp)
        ) {
            ForYouAndMeButton(
                text = configuration.text.welcome.startButton,
                backgroundColor = configuration.theme.secondaryColor.value,
                textColor = configuration.theme.primaryColorEnd.value,
                onClick = onStartClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alpha.value)
            )
        }
    }
}

@Composable
private fun getImageRatio(id: Int): Float {
    val image = painterResource(id = id)
    return image.intrinsicSize.width / image.intrinsicSize.height
}

