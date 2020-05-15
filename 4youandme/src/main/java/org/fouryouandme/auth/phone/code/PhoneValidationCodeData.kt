package org.fouryouandme.auth.phone.code

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.PhoneVerificationError

data class PhoneValidationCodeState(val configuration: Option<Configuration> = None)

sealed class PhoneValidationCodeStateUpdate {
    data class Initialization(val configuration: Configuration): PhoneValidationCodeStateUpdate()
}

sealed class PhoneValidationCodeLoading {
    object Initialization: PhoneValidationCodeLoading()
    object Auth: PhoneValidationCodeLoading()
}

sealed class PhoneValidationCodeError {
    object Initialization: PhoneValidationCodeError()
    object Auth: PhoneValidationCodeError()
}
