package org.fouryouandme.auth.consent.user

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration

data class ConsentUserState(
    val configuration: Option<Configuration> = None,
    val firstName: String = "",
    val lastName: String = ""
)

sealed class ConsentUserStateUpdate {

    data class Initialization(
        val configuration: Configuration
    ) : ConsentUserStateUpdate()

    data class FirstName(val firstName: String) : ConsentUserStateUpdate()

    data class LastName(val lastName: String) : ConsentUserStateUpdate()

}

sealed class ConsentUserLoading {

    object Initialization : ConsentUserLoading()

}

sealed class ConsentUserError {

    object Initialization : ConsentUserError()

}
