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
import com.foryouandme.domain.usecase.yourdata.GetYourDataFilterIdsUseCase
import com.foryouandme.domain.usecase.yourdata.GetYourDataUseCase
import com.foryouandme.domain.usecase.yourdata.SaveYourDataFilterIdsUseCase
import com.foryouandme.entity.yourdata.UserDataAggregation
import com.foryouandme.entity.yourdata.YourDataFilter
import com.foryouandme.entity.yourdata.YourDataPeriod
import com.foryouandme.ui.yourdata.compose.filter.YourDataFilterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class YourDataViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getYourDataUseCase: GetYourDataUseCase,
    private val getUserAggregationsUseCase: GetUserAggregationsUseCase,
    private val getYourDataFilterIdsUseCase: GetYourDataFilterIdsUseCase,
    private val saveYourDataFilterIdsUseCase: SaveYourDataFilterIdsUseCase,
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
                            userDataAggregations =
                            state.value.userDataAggregations.toLoading(),
                            userDataAggregationsFiltered =
                            state.value.userDataAggregationsFiltered.toLoading()
                        )
                    )

                    val userDataAggregations =
                        async { getUserAggregationsUseCase(state.value.period) }
                    val filtersIds =
                        async { getYourDataFilterIdsUseCase() }
                    val filters =
                        userDataAggregations
                            .await()
                            .map { data ->
                                YourDataFilterItem(
                                    filter =
                                    YourDataFilter(id = data.strategy, data.title),
                                    isSelected =
                                    if (filtersIds.await().isEmpty())
                                        true
                                    else
                                        filtersIds
                                            .await()
                                            .firstOrNull { it == data.strategy } != null
                                )
                            }
                            .distinctBy { it.filter.id }

                    state.emit(
                        state.value.copy(
                            userDataAggregations = userDataAggregations.await().toData(),
                            userDataAggregationsFiltered =
                            filterData(userDataAggregations.await(), filters).toData(),
                            periodTmp = state.value.period,
                            filters = filters,
                            filtersTmp = filters
                        )
                    )
                }
            },
            {
                state.emit(
                    state.value.copy(
                        userDataAggregations = it.toError(),
                        userDataAggregationsFiltered = it.toError()
                    )
                )
            }
        )

    /* --- period --- */

    private suspend fun selectPeriod(period: YourDataPeriod) {
        state.emit(state.value.copy(period = period))
        execute(YourDataAction.GetUserAggregations)
    }

    /* --- filters --- */

    private suspend fun setFilterPanelVisibility(isVisible: Boolean) {
        state.emit(state.value.copy(filterPanel = isVisible))
    }

    private suspend fun toggleFilter(filter: YourDataFilter) {
        val filters =
            state.value.filtersTmp.map {
                if (it.filter.id == filter.id) it.copy(isSelected = it.isSelected.not())
                else it
            }
        state.emit(state.value.copy(filtersTmp = filters))
    }

    private suspend fun selectAllFilters() {
        val filters = state.value.filtersTmp.map { it.copy(isSelected = true) }
        state.emit(state.value.copy(filtersTmp = filters))
    }

    private suspend fun clearAllFilters() {
        val filters = state.value.filtersTmp.map { it.copy(isSelected = false) }
        state.emit(state.value.copy(filtersTmp = filters))
    }

    private suspend fun saveFilters() {
        state.emit(
            state.value.copy(
                filters =
                state.value.filtersTmp,
                userDataAggregationsFiltered =
                state.value.userDataAggregations.map { filterData(it, state.value.filtersTmp) },
                filterPanel = false
            )
        )
        saveYourDataFilterIdsUseCase(
            state.value.filtersTmp
                .filter { it.isSelected }
                .map { it.filter }
        )
    }

    private fun filterData(
        data: List<UserDataAggregation>,
        filters: List<YourDataFilterItem>
    ): List<UserDataAggregation> {

        val activeFilters = filters.filter { it.isSelected }
        return data.filter {
            activeFilters.firstOrNull { item -> item.filter.id == it.strategy } != null
        }
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
            is YourDataAction.SetFilterPanelVisibility ->
                viewModelScope.launchSafe { setFilterPanelVisibility(action.isVisible) }
            is YourDataAction.ToggleFilter ->
                viewModelScope.launchSafe { toggleFilter(action.filter) }
            YourDataAction.ClearAllFilters ->
                viewModelScope.launchSafe { clearAllFilters() }
            YourDataAction.SelectAllFilters ->
                viewModelScope.launchSafe { selectAllFilters() }
            YourDataAction.SaveFilters ->
                viewModelScope.launchSafe { saveFilters() }
        }
    }

}