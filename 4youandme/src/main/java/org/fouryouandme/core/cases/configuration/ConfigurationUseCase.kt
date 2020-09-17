package org.fouryouandme.core.cases.configuration

import arrow.Kind
import arrow.core.*
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.unknownError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.cases.configuration.ConfigurationRepository.fetchConfiguration
import org.fouryouandme.core.cases.configuration.ConfigurationRepository.loadConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.core.ext.toKind

object ConfigurationUseCase {

    fun <F> getConfiguration(
        runtime: Runtime<F>,
        cachePolicy: CachePolicy
    ): Kind<F, Either<FourYouAndMeError, Configuration>> =
        runtime.fx.concurrent {

            !when (cachePolicy) {

                CachePolicy.MemoryFirst ->
                    Memory.configuration.toOption()
                        .toKind(runtime.fx) { ConfigurationRepository.loadConfiguration(runtime) }
                        .flatMap { disk ->

                            disk.fold(
                                { fetchConfiguration(runtime) },
                                { just(it.right()) }
                            )

                        }

                CachePolicy.MemoryFirstRefresh ->
                    runtime.fx.concurrent {

                        !getConfiguration(runtime, CachePolicy.MemoryFirst)
                        !fetchConfiguration(runtime)

                    }

                CachePolicy.MemoryOrDisk ->
                    runtime.fx.concurrent {

                        !Memory.configuration.toOption()
                            .toKind(runtime.fx) { ConfigurationRepository.loadConfiguration(runtime) }
                            .flatMap { disk ->

                                disk.fold(
                                    { just(unknownError(None).left()) },
                                    { just(it.right()) }
                                )

                            }

                    }

                CachePolicy.DiskFirst ->
                    ConfigurationRepository.loadConfiguration(runtime)
                        .flatMap { disk ->

                            disk.fold(
                                { fetchConfiguration(runtime) },
                                { just(it.right()) }
                            )

                        }

                CachePolicy.DiskFirstRefresh ->
                    runtime.fx.concurrent {

                        !getConfiguration(runtime, CachePolicy.DiskFirst)
                        !fetchConfiguration(runtime)

                    }

                CachePolicy.Network ->
                    fetchConfiguration(runtime)
            }
        }

    suspend fun ConfigurationModule.getConfiguration(
        cachePolicy: CachePolicy
    ): Either<FourYouAndMeError, Configuration> =

        when (cachePolicy) {

            CachePolicy.MemoryFirst -> {

                val cachedConfig = Memory.configuration ?: loadConfiguration()

                cachedConfig?.right() ?: fetchConfiguration()

            }
            CachePolicy.MemoryFirstRefresh -> {

                startCoroutineAsync { fetchConfiguration() }
                getConfiguration(CachePolicy.MemoryFirst)

            }
            CachePolicy.MemoryOrDisk ->
                (Memory.configuration ?: loadConfiguration())?.right() ?: unknownError().left()
            CachePolicy.DiskFirst ->
                loadConfiguration()?.right() ?: fetchConfiguration()
            CachePolicy.DiskFirstRefresh -> {
                startCoroutineAsync { fetchConfiguration() }
                getConfiguration(CachePolicy.DiskFirst)
            }
            CachePolicy.Network ->
                fetchConfiguration()
        }
}
