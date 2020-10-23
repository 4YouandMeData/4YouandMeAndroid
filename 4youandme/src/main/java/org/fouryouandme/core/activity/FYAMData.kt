package org.fouryouandme.core.activity

import org.fouryouandme.core.arch.livedata.Event
import org.fouryouandme.core.entity.configuration.Configuration

data class FYAMState(
    val configuration: Configuration,
    val taskId: Event<String>? = null,
    val url: Event<String>? = null,
    val openAppIntegration: Event<String>? = null
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