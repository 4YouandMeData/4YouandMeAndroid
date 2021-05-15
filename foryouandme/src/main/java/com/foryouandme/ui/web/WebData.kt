package com.foryouandme.ui.web

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration

data class WebState(val configuration: LazyData<Configuration> = LazyData.Empty) {

    companion object {

        fun mock(): WebState =
            WebState(configuration = Configuration.mock().toData())

    }

}

sealed class WebAction {

    object GetConfiguration: WebAction()
    object ScreenViewed: WebAction()

}