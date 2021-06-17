package com.foryouandme.ui.yourdata.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.yourdata.YourDataPeriod
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.verticalGradient
import com.foryouandme.ui.yourdata.YourDataAction.*
import com.foryouandme.ui.yourdata.YourDataState
import com.foryouandme.ui.yourdata.YourDataViewModel
import com.foryouandme.ui.yourdata.compose.filter.YourDataFilterItem
import com.foryouandme.ui.yourdata.compose.filter.YourDataFilters

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun YourDataPage(
    viewModel: YourDataViewModel
) {

    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect("your_data") {
        viewModel.execute(GetYourData)
        viewModel.execute(GetUserAggregations)
    }

    ForYouAndMeTheme(configuration = state.configuration) { configuration ->
        YourDataPage(
            state = state,
            configuration = configuration,
            imageConfiguration = viewModel.imageConfiguration,
            onPeriodSelected = { viewModel.execute(SelectPeriod(it)) },
            onFilterButtonClicked = { viewModel.execute(SetFilterPanelVisibility(true)) },
            onFilterCloseClicked = { viewModel.execute(SetFilterPanelVisibility(false)) },
            onFilterClicked = { viewModel.execute(ToggleFilter(it.filter)) },
            onClearAllFiltersClicked = { viewModel.execute(ClearAllFilters) },
            onSelectAllFiltersClicked = { viewModel.execute(SelectAllFilters) },
            onSaveFilterClicked = { viewModel.execute(SaveFilters) },
            onYourDataError = { viewModel.execute(GetYourData) },
            onUserAggregationsError = { viewModel.execute(GetUserAggregations) },
        )
    }

}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun YourDataPage(
    state: YourDataState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onPeriodSelected: (YourDataPeriod) -> Unit = {},
    onYourDataError: () -> Unit = {},
    onUserAggregationsError: () -> Unit = {},
    onFilterButtonClicked: () -> Unit = {},
    onFilterCloseClicked: () -> Unit = {},
    onFilterClicked: (YourDataFilterItem) -> Unit = {},
    onClearAllFiltersClicked: () -> Unit = {},
    onSelectAllFiltersClicked: () -> Unit = {},
    onSaveFilterClicked: () -> Unit = {}
) {
    StatusBar(color = configuration.theme.primaryColorStart.value)
    Box(modifier = Modifier.fillMaxSize()) {
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
                imageConfiguration = imageConfiguration,
                onPeriodSelected = onPeriodSelected,
                onFilterButtonClicked = onFilterButtonClicked,
                onYourDataError = onYourDataError,
                onUserAggregationsError = onUserAggregationsError
            )
        }
        AnimatedVisibility(
            visible = state.filterPanel,
            enter = slideInVertically({ it }, tween()),
            exit = slideOutVertically({ it }, tween()),
            modifier = Modifier.fillMaxSize()
        ) {
            YourDataFilters(
                configuration = configuration,
                imageConfiguration = imageConfiguration,
                filters = state.filtersTmp,
                onFilterClicked = onFilterClicked,
                onClearClicked = onClearAllFiltersClicked,
                onSelectAllClicked = onSelectAllFiltersClicked,
                onCloseClicked = onFilterCloseClicked,
                onSaveClicked = onSaveFilterClicked
            )
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Preview
@Composable
fun YourDataPage() {
    ForYouAndMeTheme {
        YourDataPage(
            state = YourDataState.mock(),
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}