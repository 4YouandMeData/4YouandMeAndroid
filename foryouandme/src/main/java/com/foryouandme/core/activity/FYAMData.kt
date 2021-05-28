package com.foryouandme.core.activity

import com.foryouandme.core.arch.flow.UIEvent
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.integration.IntegrationApp

data class FYAMState(
    val configuration: Configuration? = null,
    val taskId: UIEvent<String>? = null,
    val url: UIEvent<String>? = null,
    val openAppIntegration: UIEvent<IntegrationApp>? = null
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
    ) : FYAMStateEvent()

    object SendDeviceInfo : FYAMStateEvent()

}