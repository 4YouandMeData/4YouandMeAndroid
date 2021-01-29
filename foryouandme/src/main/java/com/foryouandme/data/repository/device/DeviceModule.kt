package com.foryouandme.data.repository.device

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.device.network.DeviceApi
import com.foryouandme.domain.usecase.device.DeviceRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceModule {

    @Provides
    @Singleton
    fun provideApi(environment: Environment, moshi: Moshi): DeviceApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceBindModule {

    @Binds
    abstract fun bindRepository(repository: DeviceRepositoryImpl): DeviceRepository

}