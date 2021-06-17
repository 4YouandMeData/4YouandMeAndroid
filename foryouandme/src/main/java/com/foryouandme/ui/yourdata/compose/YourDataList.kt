package com.foryouandme.ui.yourdata.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.yourdata.YourDataPeriod
import com.foryouandme.ui.compose.lazydata.LoadingError
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.yourdata.YourDataState
import com.foryouandme.ui.yourdata.compose.filter.YourDataFilterEmpty

@Composable
fun YourDataList(
    state: YourDataState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onPeriodSelected: (YourDataPeriod) -> Unit = {},
    onYourDataError: () -> Unit = {},
    onUserAggregationsError: () -> Unit = {},
    onFilterButtonClicked: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LoadingError(
            data = state.yourData,
            configuration = configuration,
            onRetryClicked = onYourDataError
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                item {
                    YourDataHeader(
                        configuration = configuration,
                        yourData = it
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item {
                    YourDataPeriodSelector(
                        configuration = configuration,
                        imageConfiguration = imageConfiguration,
                        period = state.period,
                        showFilterButton = state.userDataAggregationsFiltered.isError().not(),
                        onPeriodSelected = onPeriodSelected,
                        onFilterClicked = onFilterButtonClicked,
                        padding = PaddingValues(horizontal = 20.dp)
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                when (state.userDataAggregationsFiltered) {
                    is LazyData.Error ->
                        item {
                            YourDataError(
                                configuration = configuration,
                                error = state.userDataAggregationsFiltered.error,
                                padding = PaddingValues(horizontal = 20.dp),
                                onRetryClicked = onUserAggregationsError
                            )
                        }
                    is LazyData.Data ->
                        if (state.userDataAggregationsFiltered.value.isEmpty())
                            item {
                                YourDataFilterEmpty(
                                    configuration = configuration,
                                    padding = PaddingValues(horizontal = 20.dp),
                                    onFilterButtonClicked = onFilterButtonClicked
                                )
                            }
                        else
                            items(state.userDataAggregationsFiltered.value) {
                                YourDataChart(
                                    configuration = configuration,
                                    userData = it,
                                    period = state.period,
                                    padding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp)
                                )
                            }
                    is LazyData.Loading -> {
                        val oldValue = state.userDataAggregationsFiltered.value
                        if (oldValue != null)
                            items(oldValue) {
                                Spacer(modifier = Modifier.height(20.dp))
                                YourDataChart(
                                    configuration = configuration,
                                    userData = it,
                                    period = state.periodTmp,
                                    padding = PaddingValues(horizontal = 20.dp)
                                )
                            }
                    }
                    LazyData.Empty -> {

                    }
                }
            }
        }
        if (state.userDataAggregationsFiltered.isLoading())
            Loading(configuration = configuration, modifier = Modifier.fillMaxSize())
    }
}