package com.foryouandme.core.arch.deps.modules

import android.content.SharedPreferences
import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.configuration.network.ConfigurationApi
import com.squareup.moshi.Moshi

data class ConfigurationModule(
    val api: ConfigurationApi,
    val prefs: SharedPreferences,
    val moshi: Moshi,
    val environment: Environment,
    val errorModule: ErrorModule
)