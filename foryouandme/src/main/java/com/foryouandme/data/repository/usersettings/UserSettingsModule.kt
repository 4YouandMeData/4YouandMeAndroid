package com.foryouandme.data.repository.usersettings

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.usersettings.network.UserSettingsApi
import com.foryouandme.domain.usecase.usersettings.UserSettingsRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserSettingsModule {

    @Provides
    @Singleton
    fun provideApi(environment: Environment, moshi: Moshi): UserSettingsApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class UserSettingsBindModule {

    @Binds
    abstract fun bindRepository(repository: UserSettingsRepositoryImpl): UserSettingsRepository

}