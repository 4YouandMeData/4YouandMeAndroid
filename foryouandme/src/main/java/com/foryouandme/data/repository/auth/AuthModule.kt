package com.foryouandme.data.repository.auth

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.auth.network.AuthApi
import com.foryouandme.domain.usecase.auth.AuthRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideApi(settings: StudySettings, moshi: Moshi): AuthApi =
        getApiService(settings.getApiBaseUrl, moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindModule {

    @Binds
    abstract fun bindRepository(repository: AuthRepositoryImpl): AuthRepository

}