package com.fouryouandme.yourdata

import arrow.optics.optics
import com.fouryouandme.core.cases.yourdata.YourDataPeriod
import com.giacomoparisi.recyclerdroid.core.DroidItem

@optics
data class YourDataState(
    val items: List<DroidItem<Any>>,
    val period: YourDataPeriod
) {
    companion object
}

sealed class YourDataStateUpdate {
    data class Initialization(val items: List<DroidItem<Any>>) : YourDataStateUpdate()
    data class Period(val items: List<DroidItem<Any>>) : YourDataStateUpdate()
}

sealed class YourDataLoading {
    object Initialization : YourDataLoading()
    object Period : YourDataLoading()
}

sealed class YourDataError {
    object Initialization : YourDataError()
}