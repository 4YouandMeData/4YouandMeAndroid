package com.foryouandme.data.repository.study

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.study.network.StudyInfoApi
import com.foryouandme.domain.usecase.study.StudyInfoRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskModule {

    @Provides
    @Singleton
    fun provideApi(environment: Environment, moshi: Moshi): StudyInfoApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskBindModule {

    @Binds
    abstract fun bindRepository(repository: StudyInfoRepositoryImpl): StudyInfoRepository

}