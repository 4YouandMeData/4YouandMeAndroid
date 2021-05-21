package com.foryouandme.ui.yourdata.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.yourdata.YourDataPeriod
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.verticalGradient
import com.foryouandme.ui.yourdata.YourDataAction.*
import com.foryouandme.ui.yourdata.YourDataState
import com.foryouandme.ui.yourdata.YourDataViewModel

@Composable
fun YourDataPage(
    yourDataViewModel: YourDataViewModel = viewModel()
) {

    val state by yourDataViewModel.stateFlow.collectAsState()

    LaunchedEffect("your_data") {
        yourDataViewModel.execute(GetYourData)
        yourDataViewModel.execute(GetUserAggregations)
    }

    ForYouAndMeTheme(configuration = state.configuration) { configuration ->
        YourDataPage(
            state = state,
            configuration = configuration,
            onPeriodSelected = { yourDataViewModel.execute(SelectPeriod(it)) },
            onYourDataError = { yourDataViewModel.execute(GetYourData) },
            onUserAggregationsError = { yourDataViewModel.execute(GetUserAggregations) }
        )
    }

}

@Composable
fun YourDataPage(
    state: YourDataState,
    configuration: Configuration,
    onPeriodSelected: (YourDataPeriod) -> Unit = {},
    onYourDataError: () -> Unit = {},
    onUserAggregationsError: () -> Unit = {}
) {
    StatusBar(color = configuration.theme.primaryColorStart.value)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(configuration.theme.fourthColor.value)
    ) {
        ForYouAndMeTopAppBar(
            title = configuration.text.tab.userDataTitle,
            titleColor = configuration.theme.secondaryColor.value,
            modifier = Modifier.background(configuration.theme.verticalGradient)
        )
        YourDataList(
            state = state,
            configuration = configuration,
            onPeriodSelected = onPeriodSelected,
            onYourDataError = onYourDataError,
            onUserAggregationsError = onUserAggregationsError
        )
    }
}

@Preview
@Composable
fun YourDataPage() {
    ForYouAndMeTheme {
        YourDataPage(
            state = YourDataState.mock(),
            configuration = Configuration.mock(),
        )
    }
}