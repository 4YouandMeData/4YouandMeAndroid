package com.foryouandme.data.repository.location

import com.foryouandme.domain.usecase.location.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    abstract fun bindRepository(repository: LocationRepositoryImpl): LocationRepository

}