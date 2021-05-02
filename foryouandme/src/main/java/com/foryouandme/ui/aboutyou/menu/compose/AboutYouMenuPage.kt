package com.foryouandme.ui.aboutyou.menu.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User
import com.foryouandme.ui.aboutyou.menu.AboutYouMenuAction.GetConfiguration
import com.foryouandme.ui.aboutyou.menu.AboutYouMenuState
import com.foryouandme.ui.aboutyou.menu.AboutYouMenuViewModel
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.error.Error
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon

@Composable
fun AboutYouMenuPage(
    aboutYouMenuViewModel: AboutYouMenuViewModel = viewModel(),
    onBack: () -> Unit = {}
) {

    val state by aboutYouMenuViewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(
        state.configuration,
        { aboutYouMenuViewModel.execute(GetConfiguration) }
    ) {
        AboutYouMenuPage(
            state = state,
            configuration = it,
            onBack = onBack
        )
    }

}

@Composable
fun AboutYouMenuPage(
    state: AboutYouMenuState,
    configuration: Configuration,
    onBack: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (state.user) {
            is LazyData.Data ->
                AboutYouMenuPage(
                    user = state.user.data,
                    configuration = configuration,
                    onBack = onBack
                )
            is LazyData.Error ->
                Error(
                    error = state.user.error,
                    configuration = configuration
                )
            LazyData.Loading ->
                Loading(configuration = configuration)
            else -> {
            }
        }
    }
}

@Composable
private fun AboutYouMenuPage(
    user: User,
    configuration: Configuration,
    onBack: () -> Unit
) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(configuration.theme.secondaryColor.value)
    ) {
        ForYouAndMeTopAppBar(icon = TopAppBarIcon.CloseSecondary, onBack = onBack)
        AboutYouMenuHeader()
    }
}