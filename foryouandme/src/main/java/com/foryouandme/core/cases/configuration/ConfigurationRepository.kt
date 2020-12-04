package com.foryouandme.core.cases.configuration

import arrow.core.Either
import arrow.fx.coroutines.evalOn
import com.foryouandme.core.arch.deps.modules.ConfigurationModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.Memory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.ext.evalOnMain
import kotlinx.coroutines.Dispatchers

object ConfigurationRepository {

    /* ---------- configuration ---------- */

    private const val CONFIGURATION = "configuration"

    internal suspend fun ConfigurationModule.fetchConfiguration(
    ): Either<ForYouAndMeError, Configuration> {

        val configuration =
            errorModule.unwrapToEither { api.getConfiguration(environment.studyId()) }
                .map { it.toConfiguration() }
                .nullToError()

        configuration.map {

            saveConfiguration(it)
            evalOnMain { Memory.configuration = it }
        }

        return configuration
    }

    private suspend fun ConfigurationModule.saveConfiguration(
        configuration: Configuration?
    ): Unit {

        val configurationJson =
            configuration?.let { moshi.adapter(Configuration::class.java).toJson(it) }

        prefs.edit().putString(CONFIGURATION, configurationJson).apply()

    }

    internal suspend fun ConfigurationModule.loadConfiguration(): Configuration? {


        val configurationJson =
            prefs.getString(CONFIGURATION, null)

        val configuration =
            Either.catch {

                evalOn(Dispatchers.IO) {
                    configurationJson?.let {
                        moshi.adapter(Configuration::class.java).fromJson(it)
                    }
                }

            }.orNull()

        evalOnMain { Memory.configuration = configuration }

        return configuration
    }
}