package com.foryouandme.data.repository.study

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.study.network.StudyIApi
import com.foryouandme.domain.usecase.study.StudyRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StudyModule {

    @Provides
    @Singleton
    fun provideApi(settings: StudySettings, moshi: Moshi): StudyIApi =
        getApiService(settings.getApiBaseUrl, moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class StudyBindModule {

    @Binds
    abstract fun bindRepository(repository: StudyRepositoryImpl): StudyRepository

}