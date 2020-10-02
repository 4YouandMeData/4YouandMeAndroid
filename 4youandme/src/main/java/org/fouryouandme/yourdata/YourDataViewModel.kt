package org.fouryouandme.yourdata

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
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.yourdata.YourDataPeriod
import org.fouryouandme.core.cases.yourdata.YourDataUseCase.getUserDataAggregation
import org.fouryouandme.core.cases.yourdata.YourDataUseCase.getYourData
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.yourdata.UserDataAggregation
import org.fouryouandme.yourdata.items.YourDataButtonsItem
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

        val defaultPeriod = YourDataPeriod.Day

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
                                userAggregation.orNull()!!,
                                configuration,
                                defaultPeriod
                            ), // TODO: handle error
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
        data: List<UserDataAggregation>,
        configuration: Configuration,
        period: YourDataPeriod
    ): List<DroidItem<Any>> =
        plus(data.map { YourDataGraphItem(configuration, it, period) })

    /* --- state update --- */

    suspend fun selectPeriod(period: YourDataPeriod): Unit {

        if (state().period != period) {

            val items =
                state().items.map {
                    if (it is YourDataButtonsItem) it.copy(selectedPeriod = period)
                    else it
                }

            setStateFx(state().copy(items = items, period = period))
            { YourDataStateUpdate.Period(it.items) }

            // TODO: fetch new user aggregation data


        }

    }
}