package com.foryouandme.ui.auth.signin.pin.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.errorToast
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.auth.signin.PrivacyTermsCheckbox
import com.foryouandme.ui.auth.signin.pin.PinCodeAction
import com.foryouandme.ui.auth.signin.pin.PinCodeAction.GetConfiguration
import com.foryouandme.ui.auth.signin.pin.PinCodeAction.SetPin
import com.foryouandme.ui.auth.signin.pin.PinCodeEvent
import com.foryouandme.ui.auth.signin.pin.PinCodeState
import com.foryouandme.ui.auth.signin.pin.PinCodeViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.textfield.EntryText
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.verticalGradient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@ExperimentalComposeUiApi
@Composable
fun PinCodePage(
    viewModel: PinCodeViewModel,
    onBack: () -> Unit,
    web: (String) -> Unit = {},
    main: () -> Unit = {},
    onboarding: () -> Unit = {}
) {

    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current
    var wrongCodeError by remember { mutableStateOf(false) }
    val wrongCodeAlpha: Float by animateFloatAsState(if (wrongCodeError) 1f else 0f)
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = viewModel) {
        viewModel.eventsFlow
            .unwrapEvent("pin_code")
            .onEach {
                when (it) {
                    is PinCodeEvent.AuthError ->
                        when (it.error) {
                            is ForYouAndMeException.WrongCode -> wrongCodeError = true
                            else -> context.errorToast(it.error, state.configuration.dataOrNull())

                        }
                    PinCodeEvent.Main -> {
                        keyboardController?.hide()
                        main()
                    }
                    PinCodeEvent.Onboarding -> {
                        keyboardController?.hide()
                        onboarding()
                    }
                }
            }
            .collect()
    }

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { viewModel.execute(GetConfiguration) }
    ) { configuration ->
        PinCodePage(
            state = state,
            configuration = configuration,
            imageConfiguration = viewModel.imageConfiguration,
            wrongCodeAlpha = wrongCodeAlpha,
            onBack = onBack,
            onPinChange = {
                if (wrongCodeError) wrongCodeError = false
                viewModel.execute(SetPin(it))
            },
            onCheckedChange = { viewModel.execute(PinCodeAction.SetLegalCheckbox(it)) },
            onPrivacyClicked = { web(configuration.text.url.privacy) },
            onTermsClicked = { web((configuration.text.url.terms)) },
            onNextClicked = { viewModel.execute(PinCodeAction.Auth) }
        )
    }

}

@Composable
private fun PinCodePage(
    state: PinCodeState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    wrongCodeAlpha: Float,
    onBack: () -> Unit,
    onPinChange: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit = {},
    onPrivacyClicked: () -> Unit = {},
    onTermsClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {}
) {
    StatusBar(color = configuration.theme.primaryColorStart.value)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .background(configuration.theme.verticalGradient)
        ) {
            ForYouAndMeTopAppBar(
                imageConfiguration = imageConfiguration,
                icon = TopAppBarIcon.Back,
                modifier = Modifier.fillMaxWidth(),
                onBack = onBack
            )
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = configuration.text.phoneVerification.title,
                    style = MaterialTheme.typography.h1,
                    color = configuration.theme.secondaryColor.value,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = configuration.text.phoneVerification.body,
                    style = MaterialTheme.typography.body1,
                    color = configuration.theme.secondaryColor.value,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(30.dp))
                EntryText(
                    text = state.pin,
                    imageConfiguration = imageConfiguration,
                    labelColor = configuration.theme.secondaryColor.value,
                    placeholderColor = configuration.theme.secondaryColor.value,
                    cursorColor = configuration.theme.secondaryColor.value,
                    textColor = configuration.theme.secondaryColor.value,
                    indicatorColor = configuration.theme.secondaryColor.value,
                    iconColor = configuration.theme.secondaryColor.value,
                    isValid = state.isPinValid,
                    modifier = Modifier.fillMaxWidth(),
                    onTextChanged = onPinChange
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = configuration.text.phoneVerification.error.errorWrongCode,
                    color = configuration.theme.primaryTextColor.value,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(wrongCodeAlpha)
                )
                Spacer(modifier = Modifier.height(10.dp))
                PrivacyTermsCheckbox(
                    isChecked = state.legalCheckbox,
                    configuration = configuration,
                    modifier = Modifier.fillMaxWidth(),
                    onPrivacyClicked = onPrivacyClicked,
                    onTermsClicked = onTermsClicked,
                    onCheckedChange = onCheckedChange
                )
                Spacer(modifier = Modifier.height(30.dp))
                Image(
                    painter = painterResource(id = imageConfiguration.nextStep()),
                    contentDescription = null,
                    modifier =
                    Modifier
                        .size(50.dp)
                        .clickable { if (state.isPinValid && state.legalCheckbox) onNextClicked() }
                        .alpha(if (state.isPinValid && state.legalCheckbox) 1f else 0.5f)
                        .align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        Loading(
            configuration = configuration,
            modifier = Modifier.fillMaxSize(),
            isVisible = state.auth.isLoading()
        )
    }
}