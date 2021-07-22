package com.foryouandme.data.repository.auth.consent

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.auth.consent.network.ConsentApi
import com.foryouandme.domain.usecase.auth.consent.ConsentRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConsentModule {

    @Provides
    @Singleton
    fun provideApi(settings: StudySettings, moshi: Moshi): ConsentApi =
        getApiService(settings.getApiBaseUrl, moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class ConsentBindModule {

    @Binds
    abstract fun bindRepository(repository: ConsentRepositoryImpl): ConsentRepository

}