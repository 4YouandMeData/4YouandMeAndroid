package com.foryouandme.data.repository.analytics

import com.foryouandme.domain.usecase.analytics.AnalyticsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bindRepository(repository: AnalyticsRepositoryImpl): AnalyticsRepository

}