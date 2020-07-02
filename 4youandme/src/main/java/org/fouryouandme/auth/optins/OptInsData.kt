package org.fouryouandme.auth.optins

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.optins.OptIns

data class OptInsState(
    val configuration: Option<Configuration> = None,
    val optIns: Option<OptIns> = None
)

sealed class OptInsStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val optIns: OptIns
    ) : OptInsStateUpdate()

}

sealed class OptInsLoading {

    object Initialization : OptInsLoading()

}

sealed class OptInsError {

    object Initialization : OptInsError()

}