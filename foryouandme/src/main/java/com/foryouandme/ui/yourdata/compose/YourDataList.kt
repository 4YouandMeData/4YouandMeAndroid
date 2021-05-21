package com.foryouandme.ui.yourdata.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.yourdata.YourDataPeriod
import com.foryouandme.ui.compose.lazydata.LoadingError
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.yourdata.YourDataState

@Composable
fun YourDataList(
    state: YourDataState,
    configuration: Configuration,
    onPeriodSelected: (YourDataPeriod) -> Unit = {},
    onYourDataError: () -> Unit = {},
    onUserAggregationsError: () -> Unit = {}
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
                        period = state.period,
                        onPeriodSelected = onPeriodSelected,
                        padding = PaddingValues(horizontal = 20.dp)
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
                when (state.userDataAggregations) {
                    is LazyData.Error ->
                        item {
                            YourDataError(
                                configuration = configuration,
                                error = state.userDataAggregations.error,
                                padding = PaddingValues(horizontal = 20.dp),
                                onRetryClicked = onUserAggregationsError
                            )
                        }
                    is LazyData.Data ->
                        items(state.userDataAggregations.value) {
                            YourDataChart(
                                configuration = configuration,
                                userData = it,
                                period = state.period,
                                padding = PaddingValues(horizontal = 20.dp)
                            )
                        }
                    is LazyData.Loading -> {
                        val oldValue = state.userDataAggregations.value
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
        if (state.userDataAggregations.isLoading())
            Loading(configuration = configuration, modifier = Modifier.fillMaxSize())
    }
}