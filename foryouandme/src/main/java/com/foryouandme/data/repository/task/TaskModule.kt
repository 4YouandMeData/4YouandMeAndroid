package com.foryouandme.data.repository.task

import com.foryouandme.data.DataModule
import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.network.getApiService
import com.foryouandme.data.repository.task.network.TaskApi
import com.foryouandme.domain.usecase.task.TaskRepository
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
object TaskModule {

    @Provides
    @Singleton
    fun provideApi(environment: Environment, @Named(DataModule.TASK_MOSHI) moshi: Moshi): TaskApi =
        getApiService(environment.getApiBaseUrl(), moshi)


}

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskBindModule {

    @Binds
    abstract fun bindRepository(repository: TaskRepositoryImpl): TaskRepository

}