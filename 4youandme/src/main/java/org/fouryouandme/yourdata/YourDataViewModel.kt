package org.fouryouandme.yourdata

import arrow.core.Either
import arrow.core.toT
import arrow.fx.ForIO
import arrow.fx.coroutines.parMapN
import com.giacomoparisi.recyclerdroid.core.DroidItem
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.YourDataModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.yourdata.YourDataPeriod
import org.fouryouandme.core.cases.yourdata.YourDataUseCase.getUserDataAggregation
import org.fouryouandme.core.cases.yourdata.YourDataUseCase.getYourData
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.yourdata.UserDataAggregation
import org.fouryouandme.yourdata.items.YourDataButtonsItem
import org.fouryouandme.yourdata.items.YourDataGraphErrorItem
import org.fouryouandme.yourdata.items.YourDataGraphItem
import org.fouryouandme.yourdata.items.toYourDataHeaderItem

class YourDataViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val yourDataModule: YourDataModule
) : BaseViewModel<
        ForIO,
        YourDataState,
        YourDataStateUpdate,
        YourDataError,
        YourDataLoading>
    (
    navigator = navigator,
    runtime = runtime
) {

    /* --- data --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        configuration: Configuration,
        imageConfiguration: ImageConfiguration
    ): Unit {

        showLoadingFx(YourDataLoading.Initialization)

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
            { setErrorFx(it, YourDataError.Initialization) },
            { data ->
                setStateFx(
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

        hideLoadingFx(YourDataLoading.Initialization)
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

            showLoadingFx(YourDataLoading.Period)

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

            setStateFx(
                state().copy(
                    items = items.addGraph(userAggregationRequest, configuration, period),
                    period = period
                )
            )
            { YourDataStateUpdate.Period(it.items) }

            hideLoadingFx(YourDataLoading.Period)

        }

    }

    suspend fun updateGraphs(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit {

        showLoadingFx(YourDataLoading.Period)

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

        setStateFx(
            state().copy(
                items = items.addGraph(userAggregationRequest, configuration, state().period),
            )
        )
        { YourDataStateUpdate.Period(it.items) }

        hideLoadingFx(YourDataLoading.Period)

    }
}