package com.foryouandme.data.repository.shake

import android.content.Context
import android.hardware.SensorManager
import com.foryouandme.domain.usecase.shake.ShakeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ShakeModule {

    @Binds
    abstract fun bindRepository(repository: ShakeRepositoryImpl): ShakeRepository

}

@Module
@InstallIn(SingletonComponent::class)
object ShakeProvideModule {

    @Provides
    @Singleton
    fun provideSensorManager(@ApplicationContext application: Context): SensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

}