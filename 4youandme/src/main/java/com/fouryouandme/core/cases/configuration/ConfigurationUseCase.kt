package com.fouryouandme.core.cases.configuration

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.fouryouandme.core.arch.deps.modules.ConfigurationModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.arch.error.unknownError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.Memory
import com.fouryouandme.core.cases.configuration.ConfigurationRepository.fetchConfiguration
import com.fouryouandme.core.cases.configuration.ConfigurationRepository.loadConfiguration
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.ext.startCoroutineAsync

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
