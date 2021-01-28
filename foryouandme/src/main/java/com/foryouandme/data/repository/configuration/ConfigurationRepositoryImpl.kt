package com.foryouandme.data.repository.configuration

import android.content.SharedPreferences
import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.datasource.cache.Memory
import com.foryouandme.data.repository.configuration.network.ConfigurationApi
import com.foryouandme.domain.usecase.configuration.ConfigurationRepository
import com.foryouandme.entity.configuration.Configuration
import com.squareup.moshi.Moshi
import javax.inject.Inject

class ConfigurationRepositoryImpl @Inject constructor(
    private val api: ConfigurationApi,
    private val environment: Environment,
    private val prefs: SharedPreferences,
    private val moshi: Moshi,
    private val memory: Memory
) : ConfigurationRepository {

    override suspend fun fetchConfiguration(): Configuration =
        api.getConfiguration(environment.studyId()).toConfiguration()!!

    override suspend fun loadConfiguration(): Configuration? =

        if (memory.configuration != null) memory.configuration
        else {

            val configurationJson =
                prefs.getString(CONFIGURATION, null)

            val configuration =
                configurationJson?.let {
                    moshi.adapter(Configuration::class.java).fromJson(it)
                }

            memory.configuration = configuration

            configuration

        }

    override suspend fun saveConfiguration(configuration: Configuration) {

        val configurationJson =
            moshi.adapter(Configuration::class.java).toJson(configuration)

        prefs.edit().putString(CONFIGURATION, configurationJson).apply()

    }

    companion object {

        private const val CONFIGURATION = "configuration"

    }


}