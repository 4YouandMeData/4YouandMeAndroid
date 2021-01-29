package com.foryouandme.data.repository.device

import com.foryouandme.domain.usecase.device.DeviceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceModule {

    @Binds
    abstract fun bindRepository(repository: DeviceRepositoryImpl): DeviceRepository

}