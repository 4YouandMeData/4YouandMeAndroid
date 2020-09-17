package org.fouryouandme.core.cases.configuration

import arrow.Kind
import arrow.core.Either
import arrow.core.Option
import arrow.core.toOption
import arrow.fx.coroutines.evalOn
import kotlinx.coroutines.Dispatchers
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.deps.modules.noneToError
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.deps.onMainDispatcher
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

object ConfigurationRepository {

    /* ---------- configuration ---------- */

    private const val CONFIGURATION = "configuration"

    @Deprecated("use the suspend version")
    internal fun <F> fetchConfiguration(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, Configuration>> =
        runtime.fx.concurrent {

            val configuration =
                !runtime.injector.configurationApi
                    .getConfiguration(runtime.injector.environment.studyId())
                    .async(runtime.fx.M)
                    .attempt()
                    .unwrapToEither(runtime)
                    .mapResult(runtime.fx) { it.toConfiguration() }
                    .noneToError(runtime)

            !configuration.foldToKind(runtime.fx) {

                runtime.fx.concurrent {
                    !saveConfiguration(runtime, it.toOption())
                    !runtime.onMainDispatcher { Memory.configuration = it }
                }
            }

            configuration
        }

    internal suspend fun ConfigurationModule.fetchConfiguration(
    ): Either<FourYouAndMeError, Configuration> {

        val configuration =
            errorModule.unwrapToEither { api.getConfigurationFx(environment.studyId()) }
                .map { it.toConfiguration().orNull() }
                .noneToError()

        configuration.map {

            saveConfiguration(it)
            evalOnMain { Memory.configuration = it }
        }

        return configuration
    }

    @Deprecated("use the suspend version")
    private fun <F> saveConfiguration(
        runtime: Runtime<F>,
        configuration: Option<Configuration>
    ): Kind<F, Unit> =
        runtime.fx.concurrent {

            val configurationJson =
                configuration
                    .map { runtime.injector.moshi.adapter(Configuration::class.java).toJson(it) }
                    .orNull()

            runtime.injector.prefs.edit().putString(CONFIGURATION, configurationJson).apply()
        }

    private suspend fun ConfigurationModule.saveConfiguration(
        configuration: Configuration?
    ): Unit {

        val configurationJson =
            configuration?.let { moshi.adapter(Configuration::class.java).toJson(it) }

        prefs.edit().putString(CONFIGURATION, configurationJson).apply()

    }

    @Deprecated("use the suspend version")
    internal fun <F> loadConfiguration(runtime: Runtime<F>): Kind<F, Option<Configuration>> =
        runtime.fx.concurrent {

            val configurationJson =
                runtime.injector.prefs.getString(CONFIGURATION, null).toOption()

            val parse =
                runtime.fx.concurrent {
                    configurationJson.flatMap {
                        runtime.injector.moshi
                            .adapter(Configuration::class.java).fromJson(it)
                            .toOption()
                    }
                }

            val configuration = parse.attempt().bind().toOption().flatMap { it }

            !runtime.onMainDispatcher { Memory.configuration = configuration.orNull() }

            configuration
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