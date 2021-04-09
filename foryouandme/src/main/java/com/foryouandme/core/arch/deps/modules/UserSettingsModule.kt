package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.repository.usersettings.network.UserSettingsApi

data class UserSettingsModule(
    val api: UserSettingsApi,
    val errorModule: ErrorModule,
    val authModule: AuthModule,
    val analyticsModule: AnalyticsModule
)