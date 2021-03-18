package com.foryouandme.data.repository.feed

import com.foryouandme.data.DataModule
import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.feed.network.FeedApi
import com.foryouandme.domain.usecase.feed.FeedRepository
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
object FeedModule {

    @Provides
    @Singleton
    fun provideApi(environment: Environment, @Named(DataModule.FEED_MOSHI) moshi: Moshi): FeedApi =
        getApiService(environment.getApiBaseUrl(), moshi)

}

@Module
@InstallIn(SingletonComponent::class)
abstract class FeedBindModule {

    @Binds
    abstract fun bindRepository(repository: FeedRepositoryImpl): FeedRepository

}