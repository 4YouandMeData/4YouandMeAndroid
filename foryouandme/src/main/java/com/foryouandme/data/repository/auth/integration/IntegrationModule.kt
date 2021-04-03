package com.foryouandme.data.repository.auth.integration

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.auth.integration.network.IntegrationApi
import com.foryouandme.domain.usecase.auth.integration.IntegrationRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IntegrationModule {

    @Provides
    @Singleton
    fun provideApi(environment: Environment, moshi: Moshi): IntegrationApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class IntegrationBindModule {

    @Binds
    abstract fun bindRepository(repository: IntegrationRepositoryImpl): IntegrationRepository

}