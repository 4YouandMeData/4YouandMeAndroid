package com.foryouandme.data.repository.phase

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.phase.network.PhaseApi
import com.foryouandme.domain.usecase.phase.PhaseRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhaseModule {

    @Provides
    @Singleton
    fun provideApi(settings: StudySettings, moshi: Moshi): PhaseApi =
        getApiService(settings.getApiBaseUrl, moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class PhaseBindModule {

    @Binds
    abstract fun bindRepository(repository: PhaseRepositoryImpl): PhaseRepository

}