package com.foryouandme.app

import android.content.Context
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.data.datasource.Environment
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SampleModule {

    @Provides
    @Singleton
    fun provideEnvironment(@ApplicationContext context: Context): Environment =
        SampleEnvironment(context)

    @Provides
    @Singleton
    fun provideImageConfiguration(): ImageConfiguration =
        SampleImageConfiguration()

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)

}