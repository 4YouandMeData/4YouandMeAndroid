package com.foryouandme.data.repository.survey

import com.foryouandme.data.DataModule
import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.survey.network.SurveyApi
import com.foryouandme.domain.usecase.survey.SurveyRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SurveyModule {

    @Provides
    @Singleton
    fun provideApi(
        environment: Environment,
        @Named(DataModule.SURVEY_MOSHI) moshi: Moshi
    ): SurveyApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class SurveyBindModule {

    @Binds
    abstract fun bindRepository(repository: SurveyRepositoryImpl): SurveyRepository

}