package com.fouryouandme.yourdata

import arrow.core.Either
import arrow.core.toT
import arrow.fx.coroutines.parMapN
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.deps.modules.YourDataModule
import com.fouryouandme.core.arch.deps.modules.nullToError
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.arch.error.handleAuthError
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.RootNavController
import com.fouryouandme.core.cases.yourdata.YourDataPeriod
import com.fouryouandme.core.cases.yourdata.YourDataUseCase.getUserDataAggregation
import com.fouryouandme.core.cases.yourdata.YourDataUseCase.getYourData
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.yourdata.UserDataAggregation
import com.fouryouandme.yourdata.items.YourDataButtonsItem
import com.fouryouandme.yourdata.items.YourDataGraphErrorItem
import com.fouryouandme.yourdata.items.YourDataGraphItem
import com.fouryouandme.yourdata.items.toYourDataHeaderItem
import com.giacomoparisi.recyclerdroid.core.DroidItem
import kotlinx.coroutines.Dispatchers

class YourDataViewModel(
    navigator: Navigator,
    private val yourDataModule: YourDataModule
) : BaseViewModel<
        YourDataState,
        YourDataStateUpdate,
        YourDataError,
        YourDataLoading>
    (
    navigator = navigator,
) {

    /* --- data --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        configuration: Configuration,
    ): Unit {

        showLoading(YourDataLoading.Initialization)

        val defaultPeriod = YourDataPeriod.Week

        val yourDataRequest =
            suspend {
                yourDataModule.getYourData()
                    .nullToError()
                    .handleAuthError(rootNavController, navigator)
            }


        val userAggregationRequest =
            suspend {
                yourDataModule.getUserDataAggregation(defaultPeriod)
                    .nullToError()
                    .handleAuthError(rootNavController, navigator)
            }


        val (yourData, userAggregation) =
            parMapN(
                Dispatchers.IO,
                yourDataRequest,
                userAggregationRequest,
                { yourData,
                  userDataAggregation ->
                    yourData toT userDataAggregation
                }
            )

        yourData.fold(
            { setError(it, YourDataError.Initialization) },
            { data ->
                setState(
                    YourDataState(
                        listOf(data.toYourDataHeaderItem(configuration))
                            .addButtons(configuration, defaultPeriod)
                            .addGraph(
                                userAggregation,
                                configuration,
                                defaultPeriod
                            ),
                        defaultPeriod
                    )
                ) { YourDataStateUpdate.Initialization(it.items) }
            }
        )

        hideLoading(YourDataLoading.Initialization)
    }

    private fun List<DroidItem<Any>>.addButtons(
        configuration: Configuration,
        defaultPeriod: YourDataPeriod
    ): List<DroidItem<Any>> =
        plus(YourDataButtonsItem(configuration, "your_data_buttons", defaultPeriod))

    private fun List<DroidItem<Any>>.addGraph(
        data: Either<FourYouAndMeError, List<UserDataAggregation>>,
        configuration: Configuration,
        period: YourDataPeriod
    ): List<DroidItem<Any>> =
        data.fold(
            { plus(YourDataGraphErrorItem(configuration, "your_data_graph_error", it)) },
            { list ->
                plus(list.map { YourDataGraphItem(configuration, it, period) })
            }
        )

    /* --- state update --- */

    suspend fun selectPeriod(
        rootNavController: RootNavController,
        configuration: Configuration,
        period: YourDataPeriod
    ): Unit {

        if (state().period != period) {

            showLoading(YourDataLoading.Period)

            val items =
                state().items.mapNotNull {
                    when (it) {
                        is YourDataButtonsItem -> it.copy(selectedPeriod = period)
                        is YourDataGraphErrorItem -> null
                        is YourDataGraphItem -> null
                        else -> it
                    }
                }

            val userAggregationRequest =
                yourDataModule.getUserDataAggregation(period)
                    .nullToError()
                    .handleAuthError(rootNavController, navigator)

            setState(
                state().copy(
                    items = items.addGraph(userAggregationRequest, configuration, period),
                    period = period
                )
            )
            { YourDataStateUpdate.Period(it.items) }

            hideLoading(YourDataLoading.Period)

        }

    }

    suspend fun updateGraphs(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit {

        showLoading(YourDataLoading.Period)

        val items =
            state().items.mapNotNull {
                when (it) {
                    is YourDataGraphErrorItem -> null
                    is YourDataGraphItem -> null
                    else -> it
                }
            }

        val userAggregationRequest =
            yourDataModule.getUserDataAggregation(state().period)
                .nullToError()
                .handleAuthError(rootNavController, navigator)

        setState(
            state().copy(
                items = items.addGraph(userAggregationRequest, configuration, state().period),
            )
        )
        { YourDataStateUpdate.Period(it.items) }

        hideLoading(YourDataLoading.Period)

    }
}