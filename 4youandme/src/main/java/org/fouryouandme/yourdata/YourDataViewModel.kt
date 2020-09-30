package org.fouryouandme.yourdata

import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.yourdata.items.YourDataButtonsItem
import org.fouryouandme.yourdata.items.YourDataHeaderItem

class YourDataViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val configurationModule: ConfigurationModule
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

    suspend fun initialize(): Unit {

        val configuration =
            configurationModule.getConfiguration(CachePolicy.MemoryFirst)

        configuration.fold(
            { setErrorFx(it, YourDataError.Initialization) },
            {
                setStateFx(
                    YourDataState(it)
                ) { state ->
                    YourDataStateUpdate.Initialization(state.configuration)
                }
            }
        )
    }

    fun getItems(configuration: Configuration, imageConfiguration: ImageConfiguration) =
        listOf(
            YourDataHeaderItem(
                configuration,
                "1",
                "You’ve participated in this study for 67 days so far",
                "On average, you complete 82% of your weekly assigned tasks - Which makes you a MASTER CONTRIBUTOR to science. Thank you! Keep it up!",
                "3.7"
            ),
            YourDataButtonsItem(
                configuration,
                imageConfiguration,
                "2",
                configuration.text.yourData.dataPeriodTitle,
            )
        )
}