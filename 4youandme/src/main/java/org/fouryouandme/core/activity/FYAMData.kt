package org.fouryouandme.core.activity

import org.fouryouandme.core.entity.configuration.Configuration

data class FYAMState(
    val configuration: Configuration
)

sealed class FYAMStateUpdate {

    data class Config(val configuration: Configuration) : FYAMStateUpdate()

}

sealed class FYAMLoading {

    object Config : FYAMLoading()

}

sealed class FYAMError {

    object Config : FYAMError()

}