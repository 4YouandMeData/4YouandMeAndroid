package com.foryouandme.data.repository.permission

import com.foryouandme.domain.usecase.permission.PermissionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PermissionModule {

    @Binds
    abstract fun bindRepository(repository: PermissionRepositoryImpl): PermissionRepository

}