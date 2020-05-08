package org.fouryouandme.auth.phone

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration

data class EnterPhoneState(val configuration: Option<Configuration> = None)

sealed class EnterPhoneStateUpdate {
    data class Initialization(val configuration: Configuration): EnterPhoneStateUpdate()
}

sealed class EnterPhoneLoading {
    object Initialization: EnterPhoneLoading()
}

sealed class EnterPhoneError {
    object Initialization: EnterPhoneError()
}
