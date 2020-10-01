package org.fouryouandme.yourdata

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.YourDataModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.yourdata.YourDataUseCase.getYourData
import org.fouryouandme.core.entity.configuration.Configuration
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

        val yourData =
            yourDataModule.getYourData()
                .nullToError()
                .handleAuthError(rootNavController, navigator)

        yourData.fold(
            { setErrorFx(it, YourDataError.Initialization) },
            {
                setStateFx(
                    YourDataState(
                        listOf(it.toYourDataHeaderItem(configuration))
                            .plus(
                                getItems(
                                    configuration,
                                    imageConfiguration
                                )
                            )
                    )
                ) { YourDataStateUpdate.Initialization(it.items) }
            }
        )

        hideLoadingFx(YourDataLoading.Initialization)
    }

    fun getItems(configuration: Configuration, imageConfiguration: ImageConfiguration) =
        listOf(
            YourDataButtonsItem(
                configuration,
                imageConfiguration,
                "2",
                configuration.text.yourData.dataPeriodTitle,
            ),
            YourDataGraphItem(
                configuration,
                "3",
                "Your Weight"
            )
        )
}