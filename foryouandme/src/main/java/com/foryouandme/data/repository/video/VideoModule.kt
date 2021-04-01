package com.foryouandme.data.repository.video

import com.foryouandme.domain.usecase.video.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class VideoModule {

    @Binds
    abstract fun bindRepository(repository: VideoRepositoryImpl): VideoRepository

}