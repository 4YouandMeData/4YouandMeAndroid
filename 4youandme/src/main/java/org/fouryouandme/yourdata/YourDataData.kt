package org.fouryouandme.yourdata

import com.giacomoparisi.recyclerdroid.core.DroidItem

data class YourDataState(
    val items: List<DroidItem<Any>>
)

sealed class YourDataStateUpdate {
    data class Initialization(val items: List<DroidItem<Any>>) : YourDataStateUpdate()
}

sealed class YourDataLoading {
    object Initialization : YourDataLoading()
}

sealed class YourDataError {
    object Initialization : YourDataError()
}