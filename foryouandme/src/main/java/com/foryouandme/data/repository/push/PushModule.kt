package com.foryouandme.data.repository.push

import com.foryouandme.domain.usecase.push.PushRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PushModule {

    @Binds
    abstract fun bindRepository(repository: PushRepositoryImpl): PushRepository

}