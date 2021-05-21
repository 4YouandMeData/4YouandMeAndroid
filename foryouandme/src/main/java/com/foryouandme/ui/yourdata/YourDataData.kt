package com.foryouandme.ui.yourdata

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.yourdata.UserDataAggregation
import com.foryouandme.entity.yourdata.YourData
import com.foryouandme.entity.yourdata.YourDataPeriod

data class YourDataState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val yourData: LazyData<YourData> = LazyData.Empty,
    val userDataAggregations: LazyData<List<UserDataAggregation>> = LazyData.Empty,
    val period: YourDataPeriod = YourDataPeriod.Week,
    val periodTmp: YourDataPeriod = YourDataPeriod.Week
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
                ).toData()
            )

    }

}

sealed class YourDataAction {

    object GetConfiguration : YourDataAction()
    object GetYourData : YourDataAction()
    object GetUserAggregations : YourDataAction()
    data class SelectPeriod(val period: YourDataPeriod) : YourDataAction()

}