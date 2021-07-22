package com.foryouandme.data.repository.consent.user

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.consent.user.network.ConsentUserApi
import com.foryouandme.domain.usecase.consent.user.ConsentUserRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConsentUserModule {

    @Provides
    @Singleton
    fun provideApi(settings: StudySettings, moshi: Moshi): ConsentUserApi =
        getApiService(settings.getApiBaseUrl, moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ConsentUserBindModule {

    @Binds
    abstract fun bindRepository(repository: ConsentUserRepositoryImpl): ConsentUserRepository

}