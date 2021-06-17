package com.foryouandme.ui.yourdata

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.yourdata.UserDataAggregation
import com.foryouandme.entity.yourdata.YourData
import com.foryouandme.entity.yourdata.YourDataFilter
import com.foryouandme.entity.yourdata.YourDataPeriod
import com.foryouandme.ui.yourdata.compose.filter.YourDataFilterItem

data class YourDataState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val yourData: LazyData<YourData> = LazyData.Empty,
    val userDataAggregations: LazyData<List<UserDataAggregation>> = LazyData.Empty,
    val userDataAggregationsFiltered: LazyData<List<UserDataAggregation>> = LazyData.Empty,
    val period: YourDataPeriod = YourDataPeriod.Week,
    val periodTmp: YourDataPeriod = YourDataPeriod.Week,
    val filterPanel: Boolean = false,
    val filters: List<YourDataFilterItem> = emptyList(),
    val filtersTmp: List<YourDataFilterItem> = emptyList(),
) {

    companion object {

        fun mock(): YourDataState =
            YourDataState(
                configuration = Configuration.mock().toData(),
                yourData = YourData.mock().toData(),
                userDataAggregations =
                listOf(
                    UserDataAggregation.mock(),
                    UserDataAggregation.mock(),
                    UserDataAggregation.mock()
                ).toData(),
                userDataAggregationsFiltered =
                listOf(
                    UserDataAggregation.mock(),
                    UserDataAggregation.mock(),
                    UserDataAggregation.mock()
                ).toData()
            )

    }

}

sealed class YourDataAction {

    object GetConfiguration : YourDataAction()
    object GetYourData : YourDataAction()
    object GetUserAggregations : YourDataAction()
    data class SelectPeriod(val period: YourDataPeriod) : YourDataAction()
    data class SetFilterPanelVisibility(val isVisible: Boolean) : YourDataAction()
    data class ToggleFilter(val filter: YourDataFilter): YourDataAction()
    object ClearAllFilters: YourDataAction()
    object SelectAllFilters: YourDataAction()
    object SaveFilters: YourDataAction()

}