package com.foryouandme.data.repository.user

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.user.network.UserApi
import com.foryouandme.domain.usecase.user.UserRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideApi(environment: Environment, moshi: Moshi): UserApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class UserBindModule {

    @Binds
    abstract fun bindRepository(repository: UserRepositoryImpl): UserRepository

}