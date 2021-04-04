package com.foryouandme.data.repository.auth.consent

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.auth.consent.network.ConsentApi
import com.foryouandme.data.repository.auth.integration.network.IntegrationApi
import com.foryouandme.domain.usecase.auth.consent.ConsentRepository
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
object ConsentModule {

    @Provides
    @Singleton
    fun provideApi(environment: Environment, moshi: Moshi): ConsentApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ConsentBindModule {

    @Binds
    abstract fun bindRepository(repository: ConsentRepositoryImpl): ConsentRepository

}