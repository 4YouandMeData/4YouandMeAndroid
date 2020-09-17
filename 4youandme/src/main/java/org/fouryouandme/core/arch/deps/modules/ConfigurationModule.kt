package org.fouryouandme.core.arch.deps.modules

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.configuration.ConfigurationApi

data class ConfigurationModule(
    val api: ConfigurationApi,
    val prefs: SharedPreferences,
    val moshi: Moshi,
    val environment: Environment,
    val errorModule: ErrorModule
)