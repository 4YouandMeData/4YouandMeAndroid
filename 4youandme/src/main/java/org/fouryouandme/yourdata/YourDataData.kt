package org.fouryouandme.yourdata

import org.fouryouandme.core.entity.configuration.Configuration

data class YourDataState(
    val configuration: Configuration
)

sealed class YourDataStateUpdate {
    data class Initialization(val configuration: Configuration) : YourDataStateUpdate()
}

sealed class YourDataLoading {
    object Initialization : YourDataLoading()
}

sealed class YourDataError {
    object Initialization : YourDataError()
}