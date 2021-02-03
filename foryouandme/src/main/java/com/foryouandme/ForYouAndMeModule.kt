package com.foryouandme

import com.foryouandme.core.researchkit.task.FYAMTaskConfiguration
import com.foryouandme.researchkit.task.TaskConfiguration
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ForYouAndMeModule {

    @Binds
    abstract fun bindTaskConfiguration(taskConfiguration: FYAMTaskConfiguration): TaskConfiguration

}