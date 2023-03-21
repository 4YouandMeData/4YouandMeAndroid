package com.foryouandme.domain.usecase.configuration

import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.phase.StudyPhase

interface ConfigurationRepository {

    suspend fun fetchConfiguration(phases: List<StudyPhase>): Configuration

    suspend fun loadConfiguration(): Configuration?

    suspend fun saveConfiguration(configuration: Configuration)

}