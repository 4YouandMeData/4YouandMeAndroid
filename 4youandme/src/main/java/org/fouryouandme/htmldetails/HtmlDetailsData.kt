package org.fouryouandme.htmldetails

import org.fouryouandme.core.entity.configuration.Configuration

data class HtmlDetailsState(
    val configuration: Configuration
)

sealed class HtmlDetailsStateUpdate {
    data class Initialization(val configuration: Configuration) : HtmlDetailsStateUpdate()
}

sealed class HtmlDetailsLoading {
    object Initialization : HtmlDetailsLoading()
}

sealed class HtmlDetailsError {
    object Initialization : HtmlDetailsError()
}