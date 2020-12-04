package com.foryouandme.domain.usecase.configuration

import com.foryouandme.entity.configuration.Configuration

interface ConfigurationRepository {

    suspend fun fetchConfiguration(): Configuration

    suspend fun loadConfiguration(): Configuration?

    suspend fun saveConfiguration(configuration: Configuration)

}