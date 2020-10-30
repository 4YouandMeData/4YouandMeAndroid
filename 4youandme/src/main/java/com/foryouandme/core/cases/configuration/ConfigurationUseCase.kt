package com.foryouandme.core.cases.configuration

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.foryouandme.core.arch.deps.modules.ConfigurationModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.unknownError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.Memory
import com.foryouandme.core.cases.configuration.ConfigurationRepository.fetchConfiguration
import com.foryouandme.core.cases.configuration.ConfigurationRepository.loadConfiguration
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.startCoroutineAsync

object ConfigurationUseCase {

    suspend fun ConfigurationModule.getConfiguration(
        cachePolicy: CachePolicy
    ): Either<ForYouAndMeError, Configuration> =

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
