package com.foryouandme.ui.aboutyou.menu

import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User

data class AboutYouMenuState(
    val user: LazyData<User> = LazyData.Empty,
    val configuration: LazyData<Configuration> = LazyData.Empty
)

sealed class AboutYouMenuAction {
    object GetUser: AboutYouMenuAction()
    object GetConfiguration: AboutYouMenuAction()
    object ScreenViewed: AboutYouMenuAction()
}