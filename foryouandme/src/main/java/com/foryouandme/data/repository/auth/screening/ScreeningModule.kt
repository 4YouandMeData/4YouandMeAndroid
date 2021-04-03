package com.foryouandme.data.repository.auth.screening

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.auth.screening.network.ScreeningApi
import com.foryouandme.domain.usecase.auth.screening.ScreeningRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScreeningModule {

    @Provides
    @Singleton
    fun provideApi(environment: Environment, moshi: Moshi): ScreeningApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ScreeningBindModule {

    @Binds
    abstract fun bindRepository(repository: ScreeningRepositoryImpl): ScreeningRepository

}