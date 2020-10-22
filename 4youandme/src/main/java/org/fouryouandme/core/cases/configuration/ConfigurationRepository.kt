package org.fouryouandme.core.cases.configuration

import arrow.core.Either
import arrow.fx.coroutines.evalOn
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.evalOnMain

object ConfigurationRepository {

    /* ---------- configuration ---------- */

    private const val CONFIGURATION = "configuration"

    internal suspend fun ConfigurationModule.fetchConfiguration(
    ): Either<FourYouAndMeError, Configuration> {

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