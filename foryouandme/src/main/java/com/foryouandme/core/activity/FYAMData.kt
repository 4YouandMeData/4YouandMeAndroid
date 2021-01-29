package com.foryouandme.core.activity

import com.foryouandme.core.arch.livedata.Event
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.integration.IntegrationApp

data class FYAMState(
    val configuration: Configuration? = null,
    val taskId: Event<String>? = null,
    val url: Event<String>? = null,
    val openAppIntegration: Event<IntegrationApp>? = null
)

sealed class FYAMStateUpdate {

    data class Config(val configuration: Configuration) : FYAMStateUpdate()

}

sealed class FYAMLoading {

    object Config : FYAMLoading()
    object Splash : FYAMLoading()

}

sealed class FYAMError {

    object Config : FYAMError()

}

sealed class FYAMStateEvent {

    data class Initialize(
        val taskId: String?,
        val url: String?,
        val openAppIntegration: String?,
        val splashLoading: Boolean = false
    ): FYAMStateEvent()

}