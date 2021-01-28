package com.foryouandme.data.repository.auth

import com.foryouandme.domain.usecase.auth.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindModule {

    @Binds
    abstract fun bindRepository(repository: AuthRepositoryImpl): AuthRepository

}