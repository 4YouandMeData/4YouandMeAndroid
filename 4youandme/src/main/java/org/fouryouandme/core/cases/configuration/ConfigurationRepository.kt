package org.fouryouandme.core.cases.configuration

import arrow.Kind
import arrow.core.*
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.onMainDispatcher
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.entity.text.Text
import org.fouryouandme.core.entity.theme.HEXColor
import org.fouryouandme.core.entity.theme.HEXGradient
import org.fouryouandme.core.entity.theme.Theme

object ConfigurationRepository {

    /* ---------- theme ---------- */

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
            !fetchConfiguration(runtime)
                .map { either -> either.map { it.a } }
        }

    /* ---------- text ---------- */

    private const val TEXT = "texts"

    internal fun <F> loadText(runtime: Runtime<F>): Kind<F, Option<Text>> =
        runtime.fx.concurrent {

            val textJson =
                runtime.injector.prefs.getString(TEXT, null).toOption()

            val parse =
                runtime.fx.concurrent {
                    textJson.flatMap {
                        runtime.injector.moshi.adapter(Text::class.java).fromJson(it).toOption()
                    }
                }

            val text = parse.attempt().bind().toOption().flatMap { it }

            !runtime.onMainDispatcher { Memory.text = text }

            text
        }

    private fun <F> saveText(runtime: Runtime<F>, text: Option<Text>): Kind<F, Unit> =
        runtime.fx.concurrent {

            val textJson =
                text.map { runtime.injector.moshi.adapter(Text::class.java).toJson(it) }
                    .orNull()

            runtime.injector.prefs.edit().putString(TEXT, textJson).apply()
        }

    internal fun <F> fetchText(runtime: Runtime<F>): Kind<F, Either<FourYouAndMeError, Text>> =
        runtime.fx.concurrent {
            !fetchConfiguration(runtime)
                .map { either -> either.map { it.b } }
        }

    /* ---------- configuration ---------- */

    private fun <F> fetchConfiguration(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, Tuple2<Theme, Text>>> =
        runtime.fx.concurrent {

            // TODO: fetch from Network

            val theme =
                Theme(
                    HEXColor("#FFF"),
                    HEXColor("#FFF"),
                    HEXGradient("#FFF", "#FFF")
                ).toOption()

            val text =
                Text(
                    "Sample app",
                    "Sample body"
                ).toOption()

            !saveTheme(runtime, theme)
            !saveText(runtime, text)

            !runtime.onMainDispatcher {
                Memory.theme = theme
                Memory.text = text
            }

            theme.toEither { FourYouAndMeError.Unkonwn }
                .flatMap { themeValue ->
                    text.toEither { FourYouAndMeError.Unkonwn }
                        .map { (themeValue to it).toTuple2() }
                }
        }
}