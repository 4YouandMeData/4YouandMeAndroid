package com.foryouandme.ui.aboutyou.integration

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User

data class IntegrationLoginState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val user: LazyData<User> = LazyData.Empty
) {

    companion object {

        fun mock(): IntegrationLoginState =
            IntegrationLoginState(
                configuration = Configuration.mock().toData(),
                user = User.mock().toData()
            )

    }

}

sealed class IntegrationLoginAction {

    object GetConfiguration : IntegrationLoginAction()
    object GetUser : IntegrationLoginAction()
    object ScreenViewed : IntegrationLoginAction()

}