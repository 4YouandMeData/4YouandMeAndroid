package com.foryouandme.ui.yourdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.yourdata.GetUserAggregationsUseCase
import com.foryouandme.domain.usecase.yourdata.GetYourDataUseCase
import com.foryouandme.entity.yourdata.YourDataPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class YourDataViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getYourDataUseCase: GetYourDataUseCase,
    private val getUserAggregationsUseCase: GetUserAggregationsUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private var state = MutableStateFlow(YourDataState())
    val stateFlow = state as StateFlow<YourDataState>

    init {
        execute(YourDataAction.GetConfiguration)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = state.value.configuration.toLoading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(configuration = configuration.toData()))
                execute(YourDataAction.GetYourData)
                execute(YourDataAction.GetUserAggregations)
            },
            { state.emit(state.value.copy(configuration = it.toError())) }
        )

    /* --- your data --- */

    private fun getYourData(): Action =
        action(
            {
                state.emit(state.value.copy(yourData = state.value.yourData.toLoading()))
                val yourData = getYourDataUseCase()!!
                state.emit(state.value.copy(yourData = yourData.toData()))
            },
            { state.emit(state.value.copy(yourData = it.toError())) }
        )

    /* --- user aggregations --- */

    private fun getUserAggregations(): Action =
        action(
            {
                coroutineScope {

                    state.emit(
                        state.value.copy(
                            userDataAggregations = state.value.userDataAggregations.toLoading()
                        )
                    )

                    val userDataAggregations = getUserAggregationsUseCase(state.value.period)

                    state.emit(
                        state.value.copy(
                            userDataAggregations = userDataAggregations.toData(),
                            periodTmp = state.value.period
                        )
                    )
                }
            },
            {
                state.emit(
                    state.value.copy(
                        userDataAggregations = it.toError()
                    )
                )
            }
        )

    /* --- period --- */

    private suspend fun selectPeriod(period: YourDataPeriod) {
        state.emit(state.value.copy(period = period))
        execute(YourDataAction.GetUserAggregations)
    }

    /* --- action --- */

    fun execute(action: YourDataAction) {
        when (action) {
            YourDataAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            YourDataAction.GetYourData ->
                viewModelScope.launchAction(getYourData())
            YourDataAction.GetUserAggregations ->
                viewModelScope.launchAction(getUserAggregations())
            is YourDataAction.SelectPeriod ->
                viewModelScope.launchSafe { selectPeriod(action.period) }
        }
    }

}