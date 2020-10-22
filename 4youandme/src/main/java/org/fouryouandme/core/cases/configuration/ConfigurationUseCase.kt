package org.fouryouandme.core.cases.configuration

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.unknownError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.Memory
import org.fouryouandme.core.cases.configuration.ConfigurationRepository.fetchConfiguration
import org.fouryouandme.core.cases.configuration.ConfigurationRepository.loadConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.startCoroutineAsync

object ConfigurationUseCase {

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
