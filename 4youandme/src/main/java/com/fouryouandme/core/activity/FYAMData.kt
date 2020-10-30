package com.fouryouandme.core.activity

import com.fouryouandme.core.arch.livedata.Event
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.integration.IntegrationApp

data class FYAMState(
    val configuration: Configuration,
    val taskId: Event<String>? = null,
    val url: Event<String>? = null,
    val openAppIntegration: Event<IntegrationApp>? = null
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