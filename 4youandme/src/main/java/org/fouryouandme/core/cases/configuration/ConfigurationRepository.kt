package org.fouryouandme.core.cases.configuration

import arrow.Kind
import arrow.core.Either
import arrow.core.Option
import arrow.core.toOption
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.onMainDispatcher
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.Theme
import org.fouryouandme.core.ext.foldToKind
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.noneToError
import org.fouryouandme.core.ext.unwrapToEither

object ConfigurationRepository {

    /* ---------- configuration ---------- */

    private const val CONFIGURATION = "configuration"

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
                    .noneToError(runtime.fx)

            !configuration.foldToKind(runtime.fx) {

                runtime.fx.concurrent {
                    !saveConfiguration(runtime, it.toOption())
                    !runtime.onMainDispatcher { Memory.configuration = it.toOption() }
                }
            }

            configuration
        }

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

            !runtime.onMainDispatcher { Memory.configuration = configuration }

            configuration
        }
}