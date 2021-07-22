package com.foryouandme.data.repository.yourdata

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.yourdata.network.YourDataApi
import com.foryouandme.domain.usecase.yourdata.YourDataRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YourDataModule {

    @Provides
    @Singleton
    fun provideApi(settings: StudySettings, moshi: Moshi): YourDataApi =
        getApiService(settings.getApiBaseUrl, moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class YourDataBindModule {

    @Binds
    abstract fun bindRepository(repository: YourDataRepositoryImpl): YourDataRepository

}