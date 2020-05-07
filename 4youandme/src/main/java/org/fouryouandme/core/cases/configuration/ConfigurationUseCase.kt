package org.fouryouandme.core.cases.configuration

import arrow.Kind
import arrow.core.Either
import arrow.core.right
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.toKind

object ConfigurationUseCase {

    fun <F> getConfiguration(
        runtime: Runtime<F>,
        cachePolicy: CachePolicy
    ): Kind<F, Either<FourYouAndMeError, Configuration>> =
        runtime.fx.concurrent {

            !when (cachePolicy) {

                CachePolicy.MemoryFirst ->
                    Memory.configuration
                        .toKind(runtime.fx) { ConfigurationRepository.loadConfiguration(runtime) }
                        .flatMap { disk ->

                            disk.fold(
                                { ConfigurationRepository.fetchConfiguration(runtime) },
                                { just(it.right()) }
                            )

                        }

                CachePolicy.MemoryFirstRefresh ->
                    runtime.fx.concurrent {

                        !getConfiguration(runtime, CachePolicy.MemoryFirst)
                        !ConfigurationRepository.fetchConfiguration(runtime)

                    }

                CachePolicy.DiskFirst ->
                    ConfigurationRepository.loadConfiguration(runtime)
                        .flatMap { disk ->

                            disk.fold(
                                { ConfigurationRepository.fetchConfiguration(runtime) },
                                { just(it.right()) }
                            )

                        }

                CachePolicy.DiskFirstRefresh ->
                    runtime.fx.concurrent {

                        !getConfiguration(runtime, CachePolicy.DiskFirst)
                        !ConfigurationRepository.fetchConfiguration(runtime)

                    }

                CachePolicy.Network ->
                    ConfigurationRepository.fetchConfiguration(runtime)
            }
        }
}
