package com.foryouandme.data.repository.configuration

import com.foryouandme.core.data.api.getApiService
import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.configuration.network.ConfigurationApi
import com.foryouandme.domain.usecase.configuration.ConfigurationRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigurationModule {

    @Provides
    @Singleton
    fun provideApi(environment: Environment, moshi: Moshi): ConfigurationApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigurationBindModule {

    @Binds
    abstract fun provideRepository(
        repository: ConfigurationRepositoryImpl
    ): ConfigurationRepository

}