package org.fouryouandme.core.cases.configuration

import arrow.Kind
import arrow.core.Either
import arrow.core.Option
import arrow.core.toOption
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.onMainDispatcher
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.entity.theme.HEXColor
import org.fouryouandme.core.entity.theme.HEXGradient
import org.fouryouandme.core.entity.theme.Theme

object ConfigurationRepository {

    private const val THEME = "theme"

    internal fun <F> loadTheme(runtime: Runtime<F>): Kind<F, Option<Theme>> =
        runtime.fx.concurrent {

            val themJson =
                runtime.injector.prefs.getString(THEME, null).toOption()

            val parse =
                runtime.fx.concurrent {
                    themJson.flatMap {
                        runtime.injector.moshi.adapter(Theme::class.java).fromJson(it).toOption()
                    }
                }

            val theme = parse.attempt().bind().toOption().flatMap { it }

            !runtime.onMainDispatcher { Memory.theme = theme }

            theme
        }

    private fun <F> saveTheme(runtime: Runtime<F>, theme: Option<Theme>): Kind<F, Unit> =
        runtime.fx.concurrent {

            val themJson =
                theme.map { runtime.injector.moshi.adapter(Theme::class.java).toJson(it) }
                    .orNull()

            runtime.injector.prefs.edit().putString(THEME, themJson).apply()
        }

    internal fun <F> fetchTheme(runtime: Runtime<F>): Kind<F, Either<FourYouAndMeError, Theme>> =
        runtime.fx.concurrent {

            // TODO: fetch from Network

            val theme =
                Theme(
                    HEXColor("#FFF"),
                    HEXColor("#FFF"),
                    HEXGradient("#FFF", "#FFF")
                ).toOption()

            !saveTheme(runtime, theme)

            !runtime.onMainDispatcher { Memory.theme = theme }

            theme.toEither { FourYouAndMeError.Unkonwn }
        }
}