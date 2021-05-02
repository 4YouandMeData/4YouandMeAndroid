package com.foryouandme.data.repository.configuration

import android.content.SharedPreferences
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.configuration.network.ConfigurationApi
import com.foryouandme.domain.usecase.configuration.ConfigurationRepository
import com.foryouandme.entity.configuration.Configuration
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigurationRepositoryImpl @Inject constructor(
    private val api: ConfigurationApi,
    private val environment: Environment,
    private val prefs: SharedPreferences,
    private val moshi: Moshi,
) : ConfigurationRepository {

    // local cache
    var config: Configuration? = null

    override suspend fun fetchConfiguration(): Configuration =
        api.getConfiguration(environment.studyId()).toConfiguration()!!

    override suspend fun loadConfiguration(): Configuration? =

        if (config != null) config
        else {

            val configurationJson =
                prefs.getString(CONFIGURATION, null)

            val configuration =
                configurationJson?.let {
                    catchToNull { moshi.adapter(Configuration::class.java).fromJson(it) }
                }

            config = configuration

            configuration

        }

    override suspend fun saveConfiguration(configuration: Configuration) {

        val configurationJson =
            moshi.adapter(Configuration::class.java).toJson(configuration)

        prefs.edit().putString(CONFIGURATION, configurationJson).apply()

        memory.configuration = configuration

    }

    companion object {

        private const val CONFIGURATION = "configuration"

    }


}