package org.fouryouandme.core.cases.configuration

import arrow.Kind
import arrow.core.Either
import arrow.core.right
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.entity.text.Text
import org.fouryouandme.core.entity.theme.Theme
import org.fouryouandme.core.ext.toKind

object ConfigurationUseCase {

    fun <F> getTheme(
        runtime: Runtime<F>,
        cachePolicy: CachePolicy
    ): Kind<F, Either<FourYouAndMeError, Theme>> =
        runtime.fx.concurrent {

            !when (cachePolicy) {

                CachePolicy.MemoryFirst ->
                    Memory.theme
                        .toKind(runtime.fx) { ConfigurationRepository.loadTheme(runtime) }
                        .flatMap { disk ->

                            disk.fold(
                                { ConfigurationRepository.fetchTheme(runtime) },
                                { just(it.right()) }
                            )

                        }

                CachePolicy.MemoryFirstRefresh ->
                    runtime.fx.concurrent {

                        !getTheme(runtime, CachePolicy.MemoryFirst)
                        !ConfigurationRepository.fetchTheme(runtime)

                    }

                CachePolicy.DiskFirst ->
                    ConfigurationRepository.loadTheme(runtime)
                        .flatMap { disk ->

                            disk.fold(
                                { ConfigurationRepository.fetchTheme(runtime) },
                                { just(it.right()) }
                            )

                        }

                CachePolicy.DiskFirstRefresh ->
                    runtime.fx.concurrent {

                        !getTheme(runtime, CachePolicy.DiskFirst)
                        !ConfigurationRepository.fetchTheme(runtime)

                    }

                CachePolicy.Network ->
                    ConfigurationRepository.fetchTheme(runtime)
            }
        }

    fun <F> getText(
        runtime: Runtime<F>,
        cachePolicy: CachePolicy
    ): Kind<F, Either<FourYouAndMeError, Text>> =
        runtime.fx.concurrent {

            !when (cachePolicy) {

                CachePolicy.MemoryFirst ->
                    Memory.text
                        .toKind(runtime.fx) { ConfigurationRepository.loadText(runtime) }
                        .flatMap { disk ->

                            disk.fold(
                                { ConfigurationRepository.fetchText(runtime) },
                                { just(it.right()) }
                            )

                        }

                CachePolicy.MemoryFirstRefresh ->
                    runtime.fx.concurrent {

                        !getText(runtime, CachePolicy.MemoryFirst)
                        !ConfigurationRepository.fetchText(runtime)

                    }

                CachePolicy.DiskFirst ->
                    ConfigurationRepository.loadText(runtime)
                        .flatMap { disk ->

                            disk.fold(
                                { ConfigurationRepository.fetchText(runtime) },
                                { just(it.right()) }
                            )

                        }

                CachePolicy.DiskFirstRefresh ->
                    runtime.fx.concurrent {

                        !getText(runtime, CachePolicy.DiskFirst)
                        !ConfigurationRepository.fetchText(runtime)

                    }

                CachePolicy.Network ->
                    ConfigurationRepository.fetchText(runtime)
            }
        }
}
