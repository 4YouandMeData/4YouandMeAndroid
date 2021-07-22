package com.foryouandme.data.repository.auth.answer

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.auth.answer.network.AuthAnswerApi
import com.foryouandme.domain.usecase.auth.answer.AuthAnswerRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthAnswerModule {

    @Provides
    @Singleton
    fun provideApi(settings: StudySettings, moshi: Moshi): AuthAnswerApi =
        getApiService(settings.getApiBaseUrl, moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthAnswerBindModule {

    @Binds
    abstract fun bindRepository(repository: AuthAnswerRepositoryImpl): AuthAnswerRepository

}